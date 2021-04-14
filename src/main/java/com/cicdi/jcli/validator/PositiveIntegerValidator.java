package com.cicdi.jcli.validator;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

/**
 * @author haypo
 * @date 2021/4/14
 */
public class PositiveIntegerValidator implements IValueValidator<Integer> {
    @Override
    public void validate(String name, Integer value) throws ParameterException {
        if (value.compareTo(0) < 0) {
            throw new ParameterException(name + " can not be negative");
        }
    }
}
