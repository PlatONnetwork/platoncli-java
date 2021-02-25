package com.cicdi.jcli.converter;

import com.beust.jcommander.IStringConverter;

import java.math.BigInteger;

/**
 * @author haypo
 * @date 2021/2/24
 */
public class BigIntegerConverter implements IStringConverter<BigInteger> {


    @Override
    public BigInteger convert(String value) {
        return new BigInteger(value);
    }
}
