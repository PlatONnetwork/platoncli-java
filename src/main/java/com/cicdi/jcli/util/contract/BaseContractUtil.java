package com.cicdi.jcli.util.contract;


import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.util.*;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.crypto.CipherException;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.exceptions.TransactionException;
import com.platon.tx.RawTransactionManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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
        String jsonSchemaPath = JsonUtil.readJsonSchemaFromResource(getTemplateSchemaPath());
        if (StringUtil.isBlank(jsonSchemaPath)) {
            this.t = ParamUtil.readParam(param, clazz);
        } else {
            this.t = ParamUtil.readParam(param, clazz, jsonSchemaPath);
        }
        web3j = Web3j.build(new FastHttpService(nodeConfigModel.getRpcAddress()));
        if (isOnline) {
            String password = StringUtil.readPassword();
            credentials = WalletUtil.loadCredentials(password, address, nodeConfigModel.getHrp());
            rawTransactionManager = new RawTransactionManager(web3j, credentials);
        }
    }

    protected String getTemplateSchemaPath() {
        return null;
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

        String data = EncoderUtils.functionEncoder(function);
        return SendUtil.send(
                nodeConfigModel.getHrp(),
                getPposContractAddress(),
                data,
                //合约调用一般不发送币
                BigInteger.ZERO,
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
    public void fastSendTransaction(BigInteger gasLimit, BigInteger gasPrice) throws IOException {
        FastHttpService fastHttpService = new FastHttpService(nodeConfigModel.getRpcAddress());
        Web3j web3j = Web3j.build(fastHttpService);
        Function function = createFunction();
        String data = EncoderUtils.functionEncoder(function);
        BigInteger nonce = NonceUtil.getNonce(web3j, credentials.getAddress(), nodeConfigModel.getHrp());

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
