package com.cicdi.jcli.util;

import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.service.FastService;
import com.platon.crypto.Credentials;
import com.platon.crypto.Hash;
import com.platon.crypto.RawTransaction;
import com.platon.crypto.TransactionEncoder;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.Request;
import com.platon.protocol.core.methods.response.PlatonSendTransaction;
import com.platon.protocol.exceptions.TransactionException;
import com.platon.protocol.http.HttpService;
import com.platon.tx.exceptions.TxHashMismatchException;
import com.platon.utils.Numeric;
import com.platon.utils.TxHashVerifier;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 发送交易的工具类
 *
 * @author haypo
 * @date 2020/6/30
 */
@Slf4j
public class SendUtil {
    protected static TxHashVerifier txHashVerifier = new TxHashVerifier();

    /**
     * 发送交易
     *
     * @param hrp         hrp，币的单位
     * @param to          接收方地址
     * @param data        交易数据
     * @param vonValue    发送金额，单位为von
     * @param gasPrice    gas价格
     * @param gasLimit    gas限制
     * @param web3j       web3j
     * @param credentials 钱包
     * @param chainId     链id
     * @return tx hash
     * @throws IOException          io异常
     * @throws TransactionException 交易异常
     */
    public static String send(String hrp,
                              String to,
                              String data,
                              BigInteger vonValue,
                              BigInteger gasPrice,
                              BigInteger gasLimit,
                              Web3j web3j,
                              Credentials credentials,
                              Long chainId
    ) throws IOException, TransactionException {
        BigInteger nonce = NonceUtil.getNonce(web3j, credentials.getAddress(), hrp);
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                vonValue,
                Numeric.cleanHexPrefix(StringUtil.isBlank(data) ? "" : data));
        String signedData = SendUtil.signData(rawTransaction, credentials, chainId);

        PlatonSendTransaction ethSendTransaction = web3j.platonSendRawTransaction(signedData).send();

        if (ethSendTransaction != null && ethSendTransaction.hasError()) {
            throw new RuntimeException("Error processing transaction request: "
                    + ethSendTransaction.getError().getMessage());
        }

        if (ethSendTransaction != null && !ethSendTransaction.hasError()) {
            String txHashLocal = Hash.sha3(signedData);
            String txHashRemote = ethSendTransaction.getTransactionHash();
            if (!txHashVerifier.verify(txHashLocal, txHashRemote)) {
                throw new TxHashMismatchException(txHashLocal, txHashRemote);
            }
        }

        if (ethSendTransaction != null) {
            return ethSendTransaction.getTransactionHash();
        } else {
            throw new TransactionException("no response!");
        }
    }

    /**
     * 快速发送批量交易，并发操作。
     *
     * @param url         节点url
     * @param credentials 钱包
     * @param weiValue    发送金额，单位为wei/von
     * @param to          目标地址
     * @param data        数据
     * @param chainId     链id
     * @param gasLimit    gas限制
     * @param gasPrice    gas价格
     * @param hrp         hrp值
     * @throws IOException io异常
     */
    public static void fastSendBatch(String url, Credentials credentials, BigInteger weiValue,
                                     List<String> to, String data, long chainId,
                                     BigInteger gasLimit, BigInteger gasPrice, String hrp
    ) throws IOException {
        FastHttpService web3jService = new FastHttpService(url);
        long nonce = NonceUtil.getNonce(
                Web3j.build(web3jService), credentials.getAddress(), hrp
        ).longValue();
        for (String s : to) {
            fastSend(web3jService, weiValue, credentials, BigInteger.valueOf(nonce++), s, data, chainId, gasLimit, gasPrice);
        }
        waitForFastSend();
    }

    public static void waitForFastSend() {
        //等待线程结束后退出
        if (!Common.EXECUTOR.isShutdown()) {
            Common.EXECUTOR.shutdown();
        }
        try {
            boolean b = Common.EXECUTOR.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            if (b) {
                System.out.println("Fast send transactions finished.");
            }
        } catch (InterruptedException e) {
            log.warn("executor interrupted", e);
        }
    }

    /**
     * @param web3jService web3j
     * @param weiValue     金额
     * @param credentials  钱包
     * @param nonce        nonce值
     * @param to           目标地址
     * @param data         数据
     * @param chainId      链id
     * @param gasLimit     gas限制
     * @param gasPrice     gas价格
     */
    public synchronized static void fastSend(
            FastService web3jService,
            BigInteger weiValue,
            Credentials credentials,
            BigInteger nonce,
            String to,
            String data,
            long chainId,
            BigInteger gasLimit,
            BigInteger gasPrice) {
        Common.EXECUTOR.execute(() -> {
            try {
                RawTransaction rawTransaction = RawTransaction.createTransaction(
                        nonce,
                        gasPrice,
                        gasLimit,
                        to,
                        weiValue,
                        Numeric.cleanHexPrefix(StringUtil.isBlank(data) ? "" : data));
                byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
                String hexValue = Numeric.toHexString(signedMessage);

                Request<String, PlatonSendTransaction> request = new Request<>(
                        "platon_sendRawTransaction",
                        Collections.singletonList(hexValue),
                        web3jService,
                        PlatonSendTransaction.class);
                web3jService.fastSend(request);
                log.info("tx fast send to " + to + " succeed.");
            } catch (Exception e) {
                log.error("tx send to " + to + " failed: " + e.getMessage(), e);
            }
        });
    }

    /**
     * 对交易进行签名
     *
     * @param rawTransaction 交易
     * @param credentials    钱包
     * @param chainId        链id
     * @return 已签名交易数据
     */
    public static String signData(RawTransaction rawTransaction, Credentials credentials, long chainId) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        return Numeric.toHexString(signedMessage);
    }
    /**
     * 快速发送已签名数据列表
     *
     * @param hexValue 已签名数据
     * @param web3jService web3j对象
     */
    public static void fastSendSingedData(String hexValue, FastHttpService web3jService) throws IOException {
        Request<String, PlatonSendTransaction> request = new Request<>(
                "platon_sendRawTransaction",
                Collections.singletonList(hexValue),
                web3jService,
                PlatonSendTransaction.class);
        web3jService.fastSend(request);
    }
    /**
     * 快速发送已签名数据列表
     *
     * @param hexValueList 已签名数据列表
     * @param web3jService web3j对象
     */
    public static void fastSendSingedData(List<String> hexValueList, FastHttpService web3jService) throws IOException {
        for (String hexValue : hexValueList) {
            Request<String, PlatonSendTransaction> request = new Request<>(
                    "platon_sendRawTransaction",
                    Collections.singletonList(hexValue),
                    web3jService,
                    PlatonSendTransaction.class);
            web3jService.fastSend(request);
        }
    }

    /**
     * 发送已签名数据
     *
     * @param hexValue     已签名数据
     * @param web3jService web3j对象
     * @return 交易hash
     */
    public static String sendSingedData(String hexValue, HttpService web3jService) throws IOException {
        PlatonSendTransaction platonSendTransaction = Web3j.build(web3jService).platonSendRawTransaction(hexValue).send();
        if (platonSendTransaction.getError() != null) {
            throw new RuntimeException(platonSendTransaction.getError().getMessage());
        }
        return platonSendTransaction.getTransactionHash();
    }
}
