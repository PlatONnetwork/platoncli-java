package com.cicdi.jcli.submodule;

import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.TransactionResponse;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.methods.response.Log;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.protocol.exceptions.TransactionException;
import com.platon.rlp.solidity.RlpDecoder;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.rlp.solidity.RlpType;
import com.platon.tx.response.PollingTransactionReceiptProcessor;
import com.platon.tx.response.TransactionReceiptProcessor;
import com.platon.utils.Numeric;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.template.BaseTemplate4Serialize;
import com.cicdi.jcli.util.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

/**
 * 简单实现的解析子模块
 *
 * @author haypo
 * @date 2020/12/23
 */
@SuppressWarnings("unused")
public abstract class AbstractSimpleSubmodule implements ISubmodule {
    @Parameter(names = {"--help", "-help"}, help = true, description = "帮助")
    protected boolean help;
    @Parameter(names = {"--config", "-config", "-c"}, description = "指定交易发送的ip和端口的配置文件，全局配置network文件中若配置，可以通过指定配置中名称获取ip和port，若未填写network配置，而目录下没有节点，则报错；若没有节点，但是有多个配置节点，提示用户指明交易发送节点")
    protected String config = Common.DEFAULT_CONFIG;

    public static TransactionReceipt waitForTransactionReceipt(NodeConfigModel nodeConfigModel, String hash) throws IOException, TransactionException {
        return getProcessor(nodeConfigModel).waitForTransactionReceipt(hash);
    }

    private static TransactionReceiptProcessor getProcessor(NodeConfigModel nodeConfigModel) {
        return new PollingTransactionReceiptProcessor(
                Web3j.build(new FastHttpService(nodeConfigModel.getRpcAddress())),
                Common.DEFAULT_POLLING_FREQUENCY,
                Common.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
        );
    }

    public static Web3j createWeb3j(NodeConfigModel nodeConfigModel) {
        return Web3j.build(new FastHttpService(nodeConfigModel.getRpcAddress()));
    }

    public static <T> String genFailString(CallResponse<T> callResponse) {
        return Common.FAIL_STR + ":" + callResponse.getErrMsg();
    }

    /**
     * @return 读取web3j
     * @throws IOException io异常
     */
    public Web3j createWeb3j() throws IOException {
        return Web3j.build(new FastHttpService(ConfigUtil.readConfig(config).getRpcAddress()));
    }


    /**
     * @param function        合约方法
     * @param nodeConfigModel 节点配置
     * @param web3j           web3j对象
     * @param address         源地址
     * @param gasLimit        gas限制
     * @param gasPrice        gas价格
     * @param fast            是否快速发送
     * @return 合约交易的持久化对象
     * @throws IOException io异常
     */
    public final BaseTemplate4Serialize convert2BaseTemplate4Serialize(
            Function function, NodeConfigModel nodeConfigModel, Web3j web3j, String address,
            BigInteger gasLimit, BigInteger gasPrice, Boolean fast) throws IOException {
        String to = NetworkParametersUtil.getPposContractAddressOfProposal(nodeConfigModel.getHrp());
        String parsedAddress =AddressUtil.readAddress(address, nodeConfigModel.getHrp());
        BigInteger nonce = NonceUtil.getNonce(web3j, parsedAddress, nodeConfigModel.getHrp());
        String data = EncoderUtils.functionEncoder(function);
        return new BaseTemplate4Serialize(
                parsedAddress,
                Collections.singletonList(to),
                data,
                nonce,
                BigInteger.ZERO,
                nodeConfigModel.getChainId(),
                gasLimit,
                gasPrice,
                fast
        );
    }

    /**
     * 根据SubModule注解获得二维码图片的前缀
     *
     * @return 二维码图片的前缀
     */
    public String getQrCodeImagePrefix() {
        return this.getClass().getAnnotation(Parameters.class).commandNames()[0];
    }

    /**
     * 默认方法里会按需输出帮助信息
     *
     * @param jc   JCommander对象
     * @param argv 参数
     * @return 解析結果
     */
    @Override
    public final String parse(JCommander jc, String... argv) throws Exception {
        String result;
        if (help) {
            result = getHelpStr(jc);
        } else {
            result = run(jc, argv);
        }
        return result;
    }
}
