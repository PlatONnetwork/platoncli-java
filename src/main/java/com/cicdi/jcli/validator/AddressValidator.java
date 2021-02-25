package com.cicdi.jcli.validator;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.StringUtil;

/**
 * @author haypo
 * @date 2021/1/6
 */
public class AddressValidator implements IValueValidator<String> {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (value.length() != Common.ADDRESS_LENGTH) {
            throw new ParameterException("地址应为长度42位的字符串");
        } else if (StringUtil.isContainChinese(value)) {
            throw new ParameterException("不支持输入中文，只能输入数字和英文字母，不区分大小写，最长不得超过42位");
        }

    }
}
