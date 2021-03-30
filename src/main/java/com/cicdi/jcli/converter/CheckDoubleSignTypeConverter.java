package com.cicdi.jcli.converter;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import com.platon.contracts.ppos.dto.common.DuplicateSignType;

import java.util.Locale;

/**
 * @author haypo
 * @date 2021/3/22
 */
public class CheckDoubleSignTypeConverter implements IStringConverter<DuplicateSignType> {
    @Override
    public DuplicateSignType convert(String value) {
        switch (value.toLowerCase(Locale.ROOT)) {
            case "1":
            case "prepare_block":
                return DuplicateSignType.PREPARE_BLOCK;
            case "2":
            case "prepare_vote":
                return DuplicateSignType.PREPARE_VOTE;
            case "3":
            case "view_change":
                return DuplicateSignType.VIEW_CHANGE;
            default:
                throw new ParameterException("type参数格式错误");
        }

    }
}
