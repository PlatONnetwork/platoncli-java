package com.cicdi.jcli.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;
import com.cicdi.jcli.util.AddressUtil;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.JsonUtil;
import com.platon.crypto.WalletUtils;

/**
 * 地址校验器
 *
 * @author haypo
 * @date 2021/4/6
 */
public class AddressValidator implements IParameterValidator {
    /**
     * 校验地址
     *
     * @param name  参数名称
     * @param value 参数值
     * @throws ParameterException 参数异常
     *                            {@link WalletUtils#isValidAddress(String)}
     */
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (JsonUtil.isJsonFile(value)) {
            return;
        }
        //地址校验
        if (!WalletUtils.isValidAddress(value)) {
            throw new ParameterException("Address is invalid");
        }
    }
}
