package com.cicdi.jcli.util;

import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.methods.response.PlatonGetTransactionCount;

import java.io.IOException;
import java.math.BigInteger;

/**
 * nonce类工具
 *
 * @author haypo
 * @date 2020/12/1
 */
public class NonceUtil {
    /**
     * 获取钱包的nonce
     *
     * @param web3j       web3j对象
     * @param credentials 钱包
     * @return 当前的nonce值
     * @throws IOException web3j的io异常
     */
    public synchronized static BigInteger getNonce(Web3j web3j, Credentials credentials, String hrp) throws IOException {
        //需要对地址进行标准格式化
        String address = credentials.getAddress();
        return getNonce(web3j, address, hrp);
    }

    /**
     * 获取钱包的nonce
     *
     * @param web3j   web3j对象
     * @param address 钱包地址
     * @return 当前的nonce值
     * @throws IOException web3j的io异常
     */
    public synchronized static BigInteger getNonce(Web3j web3j, String address, String hrp) throws IOException {
        //需要对地址进行标准格式化
        String formattedAddress = AddressUtil.formatHrpAddress(address, hrp);
        PlatonGetTransactionCount count = web3j.platonGetTransactionCount(formattedAddress, DefaultBlockParameterName.PENDING).send();
        if (count.getError() != null) {
            throw new RuntimeException(count.getError().getMessage());
        }
        if (count.getTransactionCount().intValue() == 0) {
            count = web3j.platonGetTransactionCount(
                    address, DefaultBlockParameterName.LATEST).send();
        }
        return count.getTransactionCount();
    }
}
