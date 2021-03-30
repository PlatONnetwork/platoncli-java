package com.cicdi.jcli.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

/**
 * @author haypo
 * @date 2021/3/23
 */
@Slf4j
public class GasPriceUtil {
    /**
     * 对于gas价格的一些校验
     *
     * @param gasPrice gas价格
     */
    public static void verifyGasPrice(BigInteger gasPrice) {
        if (gasPrice.compareTo(BigInteger.ZERO) == 0) {
            log.warn("gas price is zero, tx transfer may still failed with timeout exception even if you are a verifier, continue? Y/N");
            if (!StringUtil.readYesOrNo()) {
                log.warn(Common.CANCEL_STR);
                System.exit(0);
            }
        }
    }
}
