package com.cicdi.jcli.validator;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;
import com.cicdi.jcli.util.AddressUtil;

/**
 * 地址校验器
 *
 * @author haypo
 * @date 2021/4/6
 */
public class AddressValidator implements IValueValidator<String> {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (AddressUtil.isJsonFile(value)) {
            return;
        }
        //地址长度需要等于42
        if (value.length() != 42) {
            throw new ParameterException(name + " length is not 42");
        }
    }
}
