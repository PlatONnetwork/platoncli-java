package com.cicdi.jcli.submodule;

import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.dto.CallResponse;
import com.alaya.contracts.ppos.dto.TransactionResponse;
import com.alaya.contracts.ppos.utils.EncoderUtils;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.Log;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.protocol.exceptions.TransactionException;
import com.alaya.rlp.solidity.RlpDecoder;
import com.alaya.rlp.solidity.RlpList;
import com.alaya.rlp.solidity.RlpString;
import com.alaya.rlp.solidity.RlpType;
import com.alaya.tx.response.PollingTransactionReceiptProcessor;
import com.alaya.tx.response.TransactionReceiptProcessor;
import com.alaya.utils.JSONUtil;
import com.alaya.utils.Numeric;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.template.BaseTemplate4Serialize;
import com.cicdi.jcli.util.*;
import com.google.zxing.WriterException;

import java.io.File;
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
public abstract class AbstractSimpleSubmodule implements ISubmodule {
    @Parameter(names = {"--help", "-help"}, help = true, description = "帮助")
    protected boolean help;
    @Parameter(names = {"--config", "-config", "-c"}, description = "指定交易发送的ip和端口的配置文件，全局配置network文件中若配置，可以通过指定配置中名称获取ip和prot，若未填写network配置，而目录下没有节点，则报错；若没有节点，但是有多个配置节点，提示用户指明交易发送节点")
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

    public static <T> String processCallResponse(CallResponse<T> callResponse) {
        if (callResponse.isStatusOk()) {
            return JsonUtil.toPrettyJsonString(callResponse.getData());
        } else {
            return genFailString(callResponse);
        }
    }

    protected static TransactionResponse getResponseFromTransactionReceipt(TransactionReceipt transactionReceipt) throws TransactionException {
        List<Log> logs = transactionReceipt.getLogs();
        if (logs == null || logs.isEmpty()) {
            throw new TransactionException("TransactionReceipt logs is empty");
        }

        String logData = logs.get(0).getData();
        if (null == logData || "".equals(logData)) {
            throw new TransactionException("TransactionReceipt log data is empty");
        }

        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logData));
        List<RlpType> rlpList = ((RlpList) (rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString) rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setCode(statusCode);
        transactionResponse.setTransactionReceipt(transactionReceipt);

        return transactionResponse;
    }

    public Web3j createWeb3j() throws IOException {
        return Web3j.build(new FastHttpService(ConfigUtil.readConfig(config).getRpcAddress()));
    }


    public final BaseTemplate4Serialize convert2BaseTemplate4Serialize(
            Function function, NodeConfigModel nodeConfigModel, Web3j web3j, String address,
            BigInteger gasLimit, BigInteger gasPrice, Boolean fast) throws IOException {
        String to = NetworkParametersUtil.getPposContractAddressOfProposal(nodeConfigModel.getHrp());
        String parsedAddress = ParamUtil.parseAddress(address, nodeConfigModel.getHrp());
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

    public String save2QrImage(
            Function function, NodeConfigModel nodeConfigModel, String address,
            BigInteger gasLimit, BigInteger gasPrice, Boolean fast) throws IOException, WriterException {
        Web3j web3j = Web3j.build(new FastHttpService(nodeConfigModel.getRpcAddress()));
        BaseTemplate4Serialize baseTemplate4Serialize = convert2BaseTemplate4Serialize(function, nodeConfigModel, web3j, address, gasLimit, gasPrice, fast);
        return QrUtil.save2QrCodeImage(getQrCodeImagePrefix(), baseTemplate4Serialize);
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
