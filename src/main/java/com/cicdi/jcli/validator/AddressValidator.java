package com.cicdi.jcli.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;
import com.cicdi.jcli.util.AddressUtil;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.JsonUtil;

/**
 * 地址校验器
 *
 * @author haypo
 * @date 2021/4/6
 */
public class AddressValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (JsonUtil.isJsonFile(value)) {
            return;
        }
        //地址长度需要等于42
        if (value.length() != Common.ADDRESS_LENGTH) {
            throw new ParameterException(name + " length is not 42");
        }
    }
}
