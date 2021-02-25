package com.cicdi.jcli.submodule.tx;

import com.alaya.contracts.ppos.dto.TransactionResponse;
import com.alaya.crypto.CipherException;
import com.alaya.crypto.Credentials;
import com.alaya.crypto.WalletUtils;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.protocol.exceptions.TransactionException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.template.BaseTemplate4Deserialize;
import com.cicdi.jcli.template.BaseTemplate4Serialize;
import com.cicdi.jcli.util.*;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

/**
 * 发送交易
 *
 * @author haypo
 * @date 2020/12/23
 */
@Slf4j
@Parameters(commandNames = "tx_transfer", commandDescription = "发送交易")
public class TransferSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--offline", "-o"}, description = "在线交易或者离线交易. 不输入默认为在线交易, 并生成二维码图片放置在桌面上，提供ATON离线扫码签名")
    protected boolean offline;
    @Parameter(names = {"--template", "-template", "-t"}, help = true, description = "查看委托交易参数模板，与其他参数共存没有效果，单独执行查看")
    protected boolean template;
    @Parameter(names = {"--param", "-param", "-p"}, description = "交易参数json字符串，或者交易参数json文件路径", required = true)
    protected String param;
    @Parameter(names = {"--address", "-address", "-d"}, description = "发送交易地址或者名称.json", required = true)
    protected String address;
    @Parameter(names = {"--fast", "-fast", "-f"}, description = "是否使用快速发送功能，默认不使用")
    protected boolean fast;

    @Override
    public String run(JCommander jc, String... argv) throws RuntimeException, IOException, CipherException, TransactionException, WriterException, InterruptedException {
        if (template && argv.length == Common.TWO) {
            return '\n' +
                    "类型             必填性         参数名称             参数解释\n" +
                    "String          must          from               发送方地址\n" +
                    "List<String>    must          to                 接收方地址\n" +
                    "BigDecimal      must          value              交易金额，单位为atp/lat\n" +
                    "String          optional      data               交易数据\n" +
                    "BigInteger      optional      gasPrice           gas价格\n" +
                    "BigInteger      optional      gasLimit           gas用量限制\n";
        }
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        BaseTemplate4Deserialize template = ParamUtil.readParam(param, BaseTemplate4Deserialize.class);
        BigInteger gasLimit = template.getGasLimit() == null ? Common.MID_GAS_LIMIT : template.getGasLimit();
        BigInteger gasPrice = template.getGasPrice() == null ? Common.MID_GAS_PRICE : template.getGasPrice();
        if (!offline && new File(address).isFile()) {
            String password = StringUtil.readPassword();
            Credentials credentials = WalletUtils.loadCredentials(password, address);
            if (fast) {
                SendUtil.fastSendBatch(
                        nodeConfigModel.getRpcAddress(),
                        credentials,
                        ConvertUtil.hrp2Von(template.getValue()),
                        template.getTo(),
                        template.getData(),
                        nodeConfigModel.getChainId(),
                        gasLimit,
                        gasPrice,
                        nodeConfigModel.getHrp()
                );
            } else {
                Web3j web3j = createWeb3j();
                for (String to : template.getTo()) {
                    try {
                        String platonSendTransaction = SendUtil.send(
                                nodeConfigModel.getHrp(),
                                to,
                                template.getData(),
                                template.getValue(),
                                gasPrice,
                                gasLimit,
                                web3j,
                                credentials,
                                nodeConfigModel.getChainId()
                        );
                        TransactionReceipt receipt = waitForTransactionReceipt(nodeConfigModel, platonSendTransaction);
                        TransactionResponse response = getResponseFromTransactionReceipt(receipt);
                        log.info(response.toString());
                    } catch (Exception e) {
                        log.error("tx send failed: " + e.getMessage(), e);
                    }
                }
            }
            return Common.SUCCESS_STR;
        } else {
            BaseTemplate4Serialize serialize = ConvertUtil.deserialize2Serialize(template, nodeConfigModel);
            serialize.setChainId(nodeConfigModel.getChainId());
            serialize.setNonce(NonceUtil.getNonce(createWeb3j(), template.getFrom(), nodeConfigModel.getHrp()));
            serialize.setFast(true);
            serialize.setGasLimit(gasLimit);
            serialize.setGasPrice(gasPrice);
            return QrUtil.save2QrCodeImage(getQrCodeImagePrefix(), serialize);
        }
    }
}
