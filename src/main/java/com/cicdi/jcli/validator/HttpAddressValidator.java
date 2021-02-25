package com.cicdi.jcli.validator;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;
import com.cicdi.jcli.util.Common;

import java.util.regex.Pattern;

/**
 * @author haypo
 * @date 2020/12/24
 */
public class HttpAddressValidator implements IValueValidator<String> {

    @Override
    public void validate(String name, String value) throws ParameterException {
        Pattern pattern = Pattern.compile(Common.HTTP_REGEX);
        if (!pattern.matcher(String.valueOf(value)).matches()) {
            throw new ParameterException("http地址格式错误!");
        }
    }
}
