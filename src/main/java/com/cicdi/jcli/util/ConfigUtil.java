package com.cicdi.jcli.util;

import com.alibaba.fastjson.JSON;
import com.cicdi.jcli.model.NodeConfigModel;
import com.platon.parameters.NetworkParameters;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author haypo
 * @date 2021/2/25
 */
@Slf4j
public class ConfigUtil {
    /**
     * 根据config读取节点配置
     *
     * @param config 配置参数，可以为文件路径或者json字符串
     * @return NodeConfigModel
     */
    public static NodeConfigModel readConfig(String config) {
        NodeConfigModel nodeConfigModel;
        try {
            File file = new File(config);
            if (file.exists() && file.isFile()) {
                nodeConfigModel = JsonUtil.readFile(file, NodeConfigModel.class,
                        JsonUtil.readJsonSchemaFromResource("/json/NodeConfigModelSchema.json"));
            } else {
                nodeConfigModel = JSON.parseObject(config, NodeConfigModel.class);
            }
            NetworkParameters.init(nodeConfigModel.getChainId(), nodeConfigModel.getHrp());
            return nodeConfigModel;
        } catch (Exception e) {
            log.error("无法读取配置文件。默认配置文件路径为config/node_config.json\nconfig模板如下：\n" +
                    "类型        必填性         参数名称          参数解释\n" +
                    "String     must          rpcAddress       节点rpc地址\n" +
                    "String     must          hrp              hrp值\n" +
                    "Long       must          chainId          节点所在的链id\n");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
