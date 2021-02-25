package com.cicdi.jcli.util;

import com.alaya.bech32.Bech32;
import com.alaya.crypto.Credentials;
import com.alaya.tx.gas.GasProvider;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.template.BaseTemplate4Deserialize;
import com.cicdi.jcli.template.BaseTemplate4Serialize;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.stream.Collectors;

/**
 * 转换器
 *
 * @author haypo
 * @date 2021/1/4
 */
public class ConvertUtil {
    /**
     * 将hrp单位值转换为von单位的值
     *
     * @param hrpValue hrp值
     * @return von单位的值
     */
    public static BigInteger hrp2Von(BigDecimal hrpValue) {
        return hrpValue.multiply(BigDecimal.TEN.pow(18)).toBigInteger();
    }

    /**
     * 将hrp单位值转换为von单位的值
     *
     * @param hrpValue hrp值
     * @return von单位的值
     */
    public static BigInteger hrp2Von(String hrpValue) {
        return new BigDecimal(hrpValue).multiply(BigDecimal.TEN.pow(18)).toBigInteger();
    }

    /**
     * 计算交易手续费
     *
     * @param gas      gas用量
     * @param gasPrice gas价格
     * @return 交易手续费
     */
    public static BigDecimal getTxFee(BigInteger gas, BigInteger gasPrice) {
        return von2Hrp(gasPrice.multiply(gas));
    }

    public static GasProvider genGasProvider(BigInteger gasLimit, BigInteger gasPrice) {
        return new GasProvider() {
            @Override
            public BigInteger getGasLimit() {
                return gasLimit;
            }

            @Override
            public BigInteger getGasPrice() {
                return gasPrice;
            }
        };
    }

    public static BigDecimal von2Hrp(BigInteger vonValue) {
        return new BigDecimal(vonValue).divide(BigDecimal.TEN.pow(18), MathContext.DECIMAL128);
    }

    /**
     * 格式化地址，例如将lax开头的地址转换为atp开头的，方便对多种钱包文件实现兼容
     *
     * @param address 原始地址
     * @param hrp     地址前缀
     * @return 符合地址前缀的格式化的地址
     */
    public static String formatHrpAddress(String address, String hrp) {
        if (address.startsWith(hrp)) {
            return address;
        }
        try {
            String hexAddress = Bech32.addressDecodeHex(address);
            return Bech32.addressEncode(hrp, hexAddress);
        } catch (Exception e) {
            return Bech32.addressEncode(hrp, address);
        }
    }

    public static String formatHrpAddress(Credentials credentials, long chainId, String hrp) {
        String hexAddress = Bech32.addressDecodeHex(credentials.getAddress(chainId));
        return Bech32.addressEncode(hrp, hexAddress);
    }

    /**
     * 将BaseTemplate4Deserialize转换为BaseTemplate4Serialize，方便后续的序列化
     *
     * @param deserialize     BaseTemplate4Deserialize
     * @param nodeConfigModel 节点配置
     * @return BaseTemplate4Serialize
     */
    public static BaseTemplate4Serialize deserialize2Serialize(BaseTemplate4Deserialize deserialize, NodeConfigModel nodeConfigModel) {
        return new BaseTemplate4Serialize(
                ConvertUtil.formatHrpAddress(deserialize.getFrom(), nodeConfigModel.getHrp()),
                deserialize.getTo().stream().map(
                        s -> ConvertUtil.formatHrpAddress(s, nodeConfigModel.getHrp())
                ).collect(Collectors.toList()),
                deserialize.getData(),
                deserialize.getNonce(),
                ConvertUtil.hrp2Von(deserialize.getValue()),
                deserialize.getChainId() == null ? nodeConfigModel.getChainId() : deserialize.getChainId(),
                deserialize.getGasLimit(),
                deserialize.getGasPrice(),
                deserialize.isFast()
        );
    }
}
