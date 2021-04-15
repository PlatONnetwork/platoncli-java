package com.cicdi.jcli.validator;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

/**
 * 账户操作类型的验证器，包含助记词和私钥两种类别的验证
 */
public class AccountOperationTypeValidator implements IValueValidator<String> {
    private static final String[] possibleValues = {"m", "mnemonic", "p", "pk", "privateKey"};

    @Override
    public void validate(String name, String value) throws ParameterException {
        boolean flag = false;
        for (String possibleVal : possibleValues) {
            if (value.equalsIgnoreCase(possibleVal)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new ParameterException("type");
        }
    }
}
