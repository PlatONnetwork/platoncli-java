package com.cicdi.jcli.util;

import com.platon.abi.solidity.FunctionEncoder;
import com.platon.abi.solidity.TypeReference;
import com.platon.abi.solidity.datatypes.Address;
import com.platon.abi.solidity.datatypes.Function;
import com.platon.abi.solidity.datatypes.Type;
import com.platon.abi.solidity.datatypes.generated.Uint256;
import com.platon.crypto.Credentials;
import com.platon.crypto.RawTransaction;
import com.platon.crypto.TransactionEncoder;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.methods.request.Transaction;
import com.platon.protocol.core.methods.response.PlatonCall;
import com.platon.protocol.core.methods.response.PlatonSendTransaction;
import com.platon.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author haypo
 * @date 2021/3/13
 */
@SuppressWarnings({"rawtypes", "unused"})
public class Erc20Util {

    /**
     * erc20 token转账，此功能目前被限制
     * RemoteCall<TransactionReceipt> transfer(String to, BigInteger value);
     *
     * @param credentials     钱包
     * @param to              目的地址
     * @param hrp             网络hrp值
     * @param gasPrice        gas价格
     * @param gasLimit        gas限制
     * @param vonValue        转账金额
     * @param contractAddress 合约地址
     * @param web3j           web3对象
     * @return 交易hash
     */
    public static String transfer(Credentials credentials, String to, String hrp, BigInteger gasPrice, BigInteger gasLimit, BigInteger vonValue, String contractAddress, Web3j web3j) throws IOException {
        Function function = new Function("transfer",
                Arrays.asList(new Address(to), new Uint256(vonValue)),
                Collections.singletonList(new TypeReference<Type>() {
                }));
        return sendTransactionReturnTxHash(function, credentials, hrp, gasPrice, gasLimit, contractAddress, web3j);
    }

    /**
     * RemoteCall<TransactionReceipt> transferFrom(String from, String to, BigInteger value);
     *
     * @param credentials     钱包
     * @param to              目的地址
     * @param hrp             hrp值
     * @param gasPrice        gas价格
     * @param gasLimit        gas限制
     * @param vonValue        金额（von）
     * @param contractAddress 合约地址
     * @param web3j           web3对象
     * @return 交易hash
     * @throws IOException web3可能抛出io异常
     */
    public static String transferFrom(Credentials credentials, String to, String hrp, BigInteger gasPrice, BigInteger gasLimit, BigInteger vonValue, String contractAddress, Web3j web3j) throws IOException {
        Function function = new Function("transferFrom",
                Arrays.asList(new Address(credentials.getAddress()), new Address(to), new Uint256(vonValue)),
                Collections.singletonList(new TypeReference<Type>() {
                }));
        return sendTransactionReturnTxHash(function, credentials, hrp, gasPrice, gasLimit, contractAddress, web3j);
    }

    public static String sendTransactionReturnTxHash(Function function, Credentials credentials, String hrp, BigInteger gasPrice, BigInteger gasLimit, String contractAddress, Web3j web3j) throws IOException {
        String encodedFunction = FunctionEncoder.encode(function);

        BigInteger nonce = NonceUtil.getNonce(web3j, credentials.getAddress(), hrp);
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, encodedFunction);
        //签名Transaction，这里要对交易做签名
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signMessage);
        //发送交易
        PlatonSendTransaction ethSendTransaction = web3j.platonSendRawTransaction(hexValue).send();
        return ethSendTransaction.getTransactionHash();
    }


    /**
     * 获得erc20 token的发行总量
     *
     * @param fromAddress     查询地址
     * @param contractAddress 合约地址
     * @param web3j           web3对象
     * @return erc20 token的发行总量
     * @throws IOException io异常
     */
    public static BigInteger getTotalSupply(String fromAddress, String contractAddress, Web3j web3j) throws IOException {
        Function function = new Function("totalSupply",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        PlatonCall ethCall = web3j.platonCall(
                Transaction.createEthCallTransaction(
                        fromAddress, contractAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .send();
        String result = ethCall.getValue();

        if (StringUtil.isBlank(result)) {
            if ("0x".equalsIgnoreCase(result) || result.length() == 2) {
                result = "0x0";
            }
        }
        return Numeric.toBigInt(result);
    }

    /**
     * 获得erc20 token的余额
     *
     * @param fromAddress     查询地址
     * @param contractAddress 合约地址
     * @param web3j           web3对象
     * @return erc20 token余额
     * @throws IOException io异常
     */
    public static BigInteger getBalanceOf(String fromAddress, String contractAddress, Web3j web3j) throws IOException {
        Function function = new Function("balanceOf",
                Collections.singletonList(new Address(fromAddress)),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        PlatonCall ethCall = web3j.platonCall(
                Transaction.createEthCallTransaction(
                        fromAddress, contractAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .send();
        String result = ethCall.getValue();

        if (StringUtil.isBlank(result)) {
            if ("0x".equalsIgnoreCase(result) || result.length() == 2) {
                result = "0x0";
            }
        }
        return Numeric.toBigInt(result);
    }
}
