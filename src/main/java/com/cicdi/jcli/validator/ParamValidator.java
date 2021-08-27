package com.cicdi.jcli.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import com.cicdi.jcli.util.JsonUtil;

/**
 * @author haypo
 * @since 2021/7/7
 */
public class ParamValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (JsonUtil.isJsonFile(value)) {
            return;
        }
        if (value.startsWith("{") && value.endsWith("}")) {
            return;
        }
        throw new ParameterException("illegal param args");
    }
}
