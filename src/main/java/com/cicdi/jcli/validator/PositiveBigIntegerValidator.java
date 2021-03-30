package com.cicdi.jcli.validator;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

import java.math.BigInteger;

/**
 * @author haypo
 * @date 2021/3/23
 */
public class PositiveBigIntegerValidator implements IValueValidator<BigInteger> {
    @Override
    public void validate(String name, BigInteger value) throws ParameterException {
        if (value.compareTo(BigInteger.ZERO) < 0) {
            throw new ParameterException(name + " can not be negative");
        }
    }
}
