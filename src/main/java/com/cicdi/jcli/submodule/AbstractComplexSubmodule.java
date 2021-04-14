package com.cicdi.jcli.submodule;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.cicdi.jcli.converter.BigIntegerConverter;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.template.BaseTemplate4Serialize;
import com.cicdi.jcli.util.*;
import com.cicdi.jcli.util.contract.BaseContractUtil;
import com.cicdi.jcli.validator.PositiveBigIntegerValidator;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.methods.response.TransactionReceipt;
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
    @Parameter(names = {"--offline", "-o"}, descriptionKey = "offline")
    protected boolean offline;
    @Parameter(names = {"--gasLimit", "-gasLimit"}, descriptionKey = "gasLimit", converter = BigIntegerConverter.class, validateValueWith = PositiveBigIntegerValidator.class)
    protected BigInteger gasLimit = Common.MID_GAS_LIMIT;
    @Parameter(names = {"--gasPrice", "-gasPrice"}, descriptionKey = "AbstractComplexSubmodule.gasPrice", converter = BigIntegerConverter.class, validateValueWith = PositiveBigIntegerValidator.class)
    protected BigInteger gasPrice = Common.MID_GAS_PRICE;
    @Parameter(names = {"--template", "-template", "-t"}, help = true, descriptionKey = "template")
    protected boolean template;
    @Parameter(names = {"--param", "-param", "-p"}, descriptionKey = "param", required = true)
    protected String param;
    @Parameter(names = {"--address", "-address", "-d"}, descriptionKey = "address", required = true)
    protected String address;
    @Parameter(names = {"--fast", "-fast", "-f"}, descriptionKey = "fast")
    protected boolean fast;

    /**
     * 该方法会产生模块对应的util
     *
     * @return 模块对应的util
     */
    public abstract FunctionUtil<U> function();

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
        String parseAddress = AddressUtil.readAddress(address, nodeConfigModel.getHrp());
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
            //打印参数模板
            return '\n' +
                    generateTemplate();
        }

        //校验gasPrice
        GasPriceUtil.verifyGasPrice(gasPrice);

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
                return TransactionReceiptUtil.handleTxReceipt(receipt);
            }
        } else {
            BaseTemplate4Serialize baseTemplate4Serialize = convert2BaseTemplate(util, nodeConfigModel);
            return QrUtil.save2QrCodeImage(getQrCodeImagePrefix(), baseTemplate4Serialize);
        }
    }
}
