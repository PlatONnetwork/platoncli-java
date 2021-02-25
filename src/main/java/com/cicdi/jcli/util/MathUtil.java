package com.cicdi.jcli.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;

/**
 * @author renhui
 * @date 2020/6/23
 */
public class MathUtil {
    /**
     * 求a，b的最大值
     *
     * @param a a值
     * @param b b值
     * @return a，b的最大值
     */
    public static BigInteger max(BigInteger a, BigInteger b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    /**
     * 求a，b的最大值
     *
     * @param a a值
     * @param b b值
     * @return a，b的最大值
     */
    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    public static int min(int a, int b) {
        return Math.min(a, b);
    }

    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    public static BigInteger min(BigInteger a, BigInteger b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    public static String roundDouble(double d, int scale) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(scale);
        return nf.format(d);
    }

    public static BigDecimal round2(BigDecimal b) {
        return round(b, 2);
    }

    public static BigDecimal round6(BigDecimal b) {
        return round(b, 6);
    }

    public static BigDecimal round(BigDecimal b, int n) {
        return b.setScale(n, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal sum(List<BigDecimal> bigDecimalList) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal bigDecimal : bigDecimalList) {
            sum = sum.add(bigDecimal);
        }
        return sum;
    }

    /**
     * 求开平方根
     *
     * @param value 底数
     * @param scale 精度
     * @return 底数的平分根
     */
    public static BigDecimal sqrt(BigDecimal value, int scale) {
        BigDecimal num2 = BigDecimal.valueOf(2);
        int precision = 100;
        MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
        BigDecimal deviation = value;
        int cnt = 0;
        while (cnt < precision) {
            deviation = (deviation.add(value.divide(deviation, mc))).divide(num2, mc);
            cnt++;
        }
        deviation = deviation.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return deviation;
    }
}
