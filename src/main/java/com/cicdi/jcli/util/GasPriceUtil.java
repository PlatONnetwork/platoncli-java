package com.cicdi.jcli.util;

import java.math.BigInteger;

/**
 * @author haypo
 * @date 2021/3/23
 */
public class GasPriceUtil {
    /**
     * 对于gas价格的一些校验
     *
     * @param gasPrice gas价格
     */
    public static void verifyGasPrice(BigInteger gasPrice) {
        if (gasPrice.compareTo(BigInteger.ZERO) == 0) {
            System.out.println(ResourceBundleUtil.getTextString("zeroGasPrice"));
            if (!StringUtil.readYesOrNo()) {
                StringUtil.warn(Common.CANCEL_STR);
                System.exit(0);
            }
        }
    }
}
