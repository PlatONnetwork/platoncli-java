package com.cicdi.jcli.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

import java.math.BigInteger;

/**
 * @author haypo
 * @date 2021/3/23
 */
public class PositiveBigIntegerValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (new BigInteger(value).compareTo(BigInteger.ZERO) < 0) {
            throw new ParameterException(name + " can not be negative");
        }
    }
}
