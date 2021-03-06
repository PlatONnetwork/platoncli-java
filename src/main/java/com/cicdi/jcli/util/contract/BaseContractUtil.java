package com.cicdi.jcli.util.contract;


import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.exception.NoSupportFunctionType;
import com.alaya.contracts.ppos.utils.EncoderUtils;
import com.alaya.crypto.CipherException;
import com.alaya.crypto.Credentials;
import com.alaya.crypto.WalletUtils;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.exceptions.TransactionException;
import com.alaya.tx.RawTransactionManager;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.util.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author haypo
 * @date 2020/3/20
 */
@Slf4j
public abstract class BaseContractUtil<T> {
    protected Web3j web3j;
    protected Credentials credentials;
    protected RawTransactionManager rawTransactionManager;
    protected NodeConfigModel nodeConfigModel;
    protected T t;

    public BaseContractUtil(boolean isOnline, String address, String config, String param, Class<T> clazz) throws IOException, CipherException {
        this.nodeConfigModel = ConfigUtil.readConfig(config);
        this.t = ParamUtil.readParam(param, clazz);
        web3j = Web3j.build(new FastHttpService(nodeConfigModel.getRpcAddress()));
        if (isOnline) {
            String password = StringUtil.readPassword();
            credentials = WalletUtils.loadCredentials(password, address);
            rawTransactionManager = new RawTransactionManager(web3j, credentials, nodeConfigModel.getChainId());
        }
    }


    /**
     * 获取合约方法
     *
     * @return 构建合约方法
     * @throws IOException io异常
     */
    public abstract Function createFunction() throws IOException;

    /**
     * 获取ppos合约地址
     *
     * @return 合约地址
     */
    public abstract String getPposContractAddress();

    /**
     * 发送合约交易
     *
     * @return 交易hash
     * @throws IOException          会抛出一些异常
     * @throws TransactionException 交易异常
     */
    public String sendTransaction(BigInteger gasLimit, BigInteger gasPrice) throws IOException, TransactionException {
        FastHttpService fastHttpService = new FastHttpService(nodeConfigModel.getRpcAddress());
        Web3j web3j = Web3j.build(fastHttpService);
        Function function = createFunction();

        BigInteger estimateGasLimit = Common.getDefaultGasProvider(function).getGasLimit();
        gasLimit = MathUtil.max(gasLimit, estimateGasLimit);

        String data = EncoderUtils.functionEncoder(function);
        return SendUtil.send(
                nodeConfigModel.getHrp(),
                getPposContractAddress(),
                data,
                //合约调用一般不发送币
                BigDecimal.ZERO,
                //一般合约调用的参数里没有gas设置，此处采用默认的配置文件里的gas设置
                gasPrice,
                gasLimit,
                web3j,
                credentials,
                nodeConfigModel.getChainId()
        );
    }

    /**
     * 快速发送合约交易
     *
     * @throws IOException 会抛出一些异常
     */
    public void fastSendTransaction(BigInteger gasLimit, BigInteger gasPrice) throws IOException, NoSupportFunctionType {
        FastHttpService fastHttpService = new FastHttpService(nodeConfigModel.getRpcAddress());
        Web3j web3j = Web3j.build(fastHttpService);
        Function function = createFunction();
        String data = EncoderUtils.functionEncoder(function);
        BigInteger nonce = NonceUtil.getNonce(web3j, credentials.getAddress(), nodeConfigModel.getHrp());

        BigInteger estimateGasLimit = Common.getDefaultGasProvider(function).getGasLimit();
        gasLimit = MathUtil.max(gasLimit, estimateGasLimit);

        SendUtil.fastSend(
                new FastHttpService(nodeConfigModel.getRpcAddress()),
                BigInteger.ZERO,
                credentials,
                nonce,
                getPposContractAddress(),
                data,
                nodeConfigModel.getChainId(),
                gasLimit,
                gasPrice
        );
        SendUtil.waitForFastSend();
    }
}
