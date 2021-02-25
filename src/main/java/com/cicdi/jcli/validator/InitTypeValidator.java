package com.cicdi.jcli.validator;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

/**
 * @author haypo
 * @date 2021/1/3
 */
public class InitTypeValidator implements IValueValidator<String> {
    @Override
    public void validate(String name, String value) throws ParameterException {
        String valueVal = String.valueOf(value);
        if ("m".equals(valueVal) || "t".equals(valueVal) || "p".equals(valueVal)) {
            return;
        }
        throw new ParameterException("type格式错误!");
    }
}
