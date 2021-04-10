package com.cicdi.jcli.submodule.tx;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.template.BaseTemplate4Deserialize;
import com.cicdi.jcli.template.BaseTemplate4Serialize;
import com.cicdi.jcli.util.*;
import com.google.zxing.WriterException;
import com.platon.crypto.CipherException;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.methods.response.TransactionReceipt;
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
@SuppressWarnings("unused")
@Parameters(commandNames = "tx_transfer", resourceBundle = "command", commandDescription = "发送交易")
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

    public String getTemplate() {
        return '\n' +
                "类型             必填性         参数名称             参数解释\n" +
                "String          must          from               发送方地址\n" +
                "List<String>    must          to                 接收方地址\n" +
                "BigDecimal      must          value              交易金额，单位为atp/lat\n" +
                "String          optional      data               交易数据\n" +
                "BigInteger      optional      gasPrice           gas价格\n" +
                "BigInteger      optional      gasLimit           gas用量限制\n";
    }

    /**
     * @param jc   jc对象
     * @param argv 参数
     * @return 交易发送结果
     * @throws IOException     param读取错误
     * @throws CipherException 钱包密码不正确
     * @throws WriterException 写入二维码失败
     */
    @Override
    public String run(JCommander jc, String... argv) throws IOException, CipherException, WriterException {
        if (template && argv.length == Common.TWO) {
            return getTemplate();
        }
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        String schema = JsonUtil.readJsonSchemaFromResource("/json/BaseTemplate4DeserializeSchema.json");
        BaseTemplate4Deserialize template = ParamUtil.readParam(param, BaseTemplate4Deserialize.class, schema);

        if (!StringUtil.isBlank(template.getData())) {
            System.out.println("The data is not null and this may cause more gas used, continue? Y/N");
            if (!StringUtil.readYesOrNo()) {
                return Common.CANCEL_STR;
            }
        }

        BigInteger vonValue = ConvertUtil.hrp2Von(template.getValue());
        if (vonValue.compareTo(BigInteger.ZERO) == 0) {
            System.out.println("transfer value is zero, continue? Y/N");
            if (!StringUtil.readYesOrNo()) {
                return Common.CANCEL_STR;
            }
        }

        //设置gas
        BigInteger gasLimit = template.getGasLimit() == null ? Common.MID_GAS_LIMIT : template.getGasLimit();
        BigInteger gasPrice = template.getGasPrice() == null ? Common.MID_GAS_PRICE : template.getGasPrice();

        //校验gasPrice
        GasPriceUtil.verifyGasPrice(gasPrice);

        if (template.getChainId() != null) {
            //校验chainId设置
            if (!nodeConfigModel.getChainId().equals(template.getChainId())) {
                System.out.println("The chainId in param is not equal as node_config, so the transfer may failed, continue? Y/N");
                if (!StringUtil.readYesOrNo()) {
                    return Common.CANCEL_STR;
                }
            }
        }

        if (!offline && new File(address).isFile()) {
            //在线交易
            String password = StringUtil.readPassword();
            Credentials credentials = WalletUtil.loadCredentials(password, address, nodeConfigModel.getHrp());
            if (fast) {
                SendUtil.fastSendBatch(nodeConfigModel.getRpcAddress(), credentials, vonValue,
                        template.getTo(), template.getData(), nodeConfigModel.getChainId(), gasLimit,
                        gasPrice, nodeConfigModel.getHrp()
                );
                return Common.SUCCESS_STR;
            } else {
                Web3j web3j = createWeb3j();
                boolean flag = true;
                for (String to : template.getTo()) {
                    try {
                        String platonSendTransaction = SendUtil.send(nodeConfigModel.getHrp(),
                                to, template.getData(), vonValue,
                                gasPrice, gasLimit,
                                web3j, credentials, nodeConfigModel.getChainId()
                        );
                        TransactionReceipt receipt = waitForTransactionReceipt(nodeConfigModel, platonSendTransaction);
                        log.info(TransactionReceiptUtil.handleTxReceipt(receipt));
                        if (!receipt.isStatusOK()) {
                            flag = false;
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        throw new RuntimeException("The transaction send failed");
                    }
                }
                return flag ? Common.SUCCESS_STR : Common.FAIL_STR;
            }
        } else {
            BaseTemplate4Serialize serialize = ConvertUtil.deserialize2Serialize(template, nodeConfigModel);
            serialize.setChainId(nodeConfigModel.getChainId());
            serialize.setNonce(NonceUtil.getNonce(createWeb3j(), template.getFrom(), nodeConfigModel.getHrp()));
            serialize.setFast(fast);
            serialize.setGasLimit(gasLimit);
            serialize.setGasPrice(gasPrice);
            return QrUtil.save2QrCodeImage(getQrCodeImagePrefix(), serialize);
        }
    }
}
