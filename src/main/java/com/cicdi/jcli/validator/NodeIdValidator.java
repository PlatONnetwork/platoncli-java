package com.cicdi.jcli.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import com.cicdi.jcli.util.Common;
import com.platon.utils.Numeric;

/**
 * @author haypo
 * @date 2021/4/20
 */
public class NodeIdValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (Numeric.containsHexPrefix(value) && value.length() != Common.MAX_NODE_ID_LENGTH) {
            throw new ParameterException(name + " length is not " + Common.MAX_NODE_ID_LENGTH);
        }
        if (!Numeric.containsHexPrefix(value) && value.length() != Common.MIN_NODE_ID_LENGTH) {
            throw new ParameterException(name + " length is not " + Common.MIN_NODE_ID_LENGTH);
        }
    }
}
