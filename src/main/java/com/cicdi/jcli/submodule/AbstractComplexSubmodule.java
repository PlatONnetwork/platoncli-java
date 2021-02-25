package com.cicdi.jcli.submodule;

import com.alaya.contracts.ppos.dto.TransactionResponse;
import com.alaya.contracts.ppos.utils.EncoderUtils;
import com.alaya.crypto.CipherException;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.cicdi.jcli.converter.BigIntegerConverter;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.template.BaseTemplate4Serialize;
import com.cicdi.jcli.util.*;
import com.cicdi.jcli.util.contract.BaseContractUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;

/**
 * 复杂实现的解析子模块
 *
 * @author haypo
 * @date 2020/12/23
 */
@Slf4j
public abstract class AbstractComplexSubmodule<T, U extends BaseContractUtil<T>> extends AbstractSimpleSubmodule {
    @Parameter(names = {"--offline", "-o"}, description = "在线交易或者离线交易. 不输入默认为在线交易, 并生成二维码图片放置在桌面上，提供ATON离线扫码签名")
    protected boolean offline;
    @Parameter(names = {"--gasLimit", "-gasLimit"}, description = "gas用量限制", converter = BigIntegerConverter.class)
    protected BigInteger gasLimit = Common.MID_GAS_LIMIT;
    @Parameter(names = {"--gasPrice", "-gasPrice"}, description = "gas价格", converter = BigIntegerConverter.class)
    protected BigInteger gasPrice = Common.MID_GAS_PRICE;
    @Parameter(names = {"--template", "-template", "-t"}, help = true, description = "查看委托交易参数模板，与其他参数共存没有效果，单独执行查看")
    protected boolean template;
    @Parameter(names = {"--param", "-param", "-p"}, description = "交易参数json字符串，或者交易参数json文件路径", required = true)
    protected String param;
    @Parameter(names = {"--address", "-address", "-d"}, description = "发送交易地址或者名称.json", required = true)
    protected String address;
    @Parameter(names = {"--fast", "-fast", "-f"}, description = "是否使用快速发送功能，默认不使用")
    protected boolean fast;

    /**
     * 该方法会产生模块对应的util
     *
     * @return 模块对应的util
     * @throws IOException     io异常
     * @throws CipherException 密码异常
     */
    public abstract FunctionUtil<U> function() throws IOException, CipherException;

    /**
     * 生成模板说明，以供打印
     *
     * @return 模板说明
     */
    public abstract String generateTemplate();

    public boolean isOnline() {
        if (offline || address == null) {
            return false;
        }
        return new File(address).isFile();
    }

    /**
     * 将模板转换为基本模板，方便写入二维码
     *
     * @param nodeConfigModel 节点配置
     * @return 基本模板
     * @throws IOException io异常
     */
    public BaseTemplate4Serialize convert2BaseTemplate(U util, NodeConfigModel nodeConfigModel) throws IOException {
        String to = util.getPposContractAddress();
        Web3j web3j = Web3j.build(new FastHttpService(nodeConfigModel.getRpcAddress()));
        String parseAddress = ParamUtil.parseAddress(address, nodeConfigModel.getHrp());
        BigInteger nonce = NonceUtil.getNonce(web3j, parseAddress, nodeConfigModel.getHrp());
        return new BaseTemplate4Serialize(
                parseAddress,
                Collections.singletonList(to),
                EncoderUtils.functionEncoder(util.createFunction()),
                nonce,
                BigInteger.ZERO,
                nodeConfigModel.getChainId(),
                gasLimit,
                gasPrice,
                fast
        );
    }


    /**
     * 实现cli中的各种方法
     *
     * @param jc   jc对象
     * @param argv 参数
     * @return cli方法返回结果
     * @throws Exception 一些异常
     */
    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        if (template && argv.length == Common.TWO) {
            return '\n' +
                    generateTemplate();
        }
        U util = function().genU();
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        if (isOnline()) {
            if (fast) {
                try {
                    util.fastSendTransaction(gasLimit, gasPrice);
                    return Common.SUCCESS_STR;
                } catch (Exception e) {
                    return Common.FAIL_STR + ": " + e.getMessage();
                }
            } else {
                String txHash = util.sendTransaction(gasLimit, gasPrice);
                TransactionReceipt receipt = waitForTransactionReceipt(nodeConfigModel, txHash);
                TransactionResponse response = getResponseFromTransactionReceipt(receipt);
                return response.isStatusOk() ?
                        Common.SUCCESS_STR + ", tx hash: " + txHash :
                        Common.FAIL_STR + ": " + response.getTransactionReceipt();
            }
        } else {
            BaseTemplate4Serialize baseTemplate4Serialize = convert2BaseTemplate(util, nodeConfigModel);
            return QrUtil.save2QrCodeImage(getQrCodeImagePrefix(), baseTemplate4Serialize);
        }
    }
}
