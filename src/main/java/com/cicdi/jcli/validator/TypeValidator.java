package com.cicdi.jcli.validator;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

import java.math.BigInteger;

/**
 * @author haypo
 * @date 2021/1/6
 */
public class TypeValidator implements IValueValidator<BigInteger> {
    @Override
    public void validate(String name, BigInteger value) throws ParameterException {
        if (value.longValue() != 1 && value.longValue() != 0) {
            throw new ParameterException("数字长度为1，只能填入1或者0");
        }
    }
}
