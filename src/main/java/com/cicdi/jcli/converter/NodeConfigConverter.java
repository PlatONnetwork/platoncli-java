package com.cicdi.jcli.converter;

import com.beust.jcommander.IStringConverter;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.util.ConfigUtil;

/**
 * @author haypo
 * @date 2021/2/25
 */
public class NodeConfigConverter implements IStringConverter<NodeConfigModel> {
    @Override
    public NodeConfigModel convert(String value) {
        return ConfigUtil.readConfig(value);
    }
}
