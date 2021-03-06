package com.cicdi.jcli.util;

import com.cicdi.jcli.model.NodeConfigModel;

import java.io.File;

/**
 * @author haypo
 * @date 2021/2/25
 */
public class ConfigUtil {

    /**
     * 根据config读取节点配置
     *
     * @param config 配置参数，可以为文件路径或者json字符串
     * @return NodeConfigModel
     */
    public static NodeConfigModel readConfig(String config) {
        try {
            File file = new File(config);
            if (file.exists() && file.isFile()) {
                return JsonUtil.readFile(config, NodeConfigModel.class);
            } else {
                return JsonUtil.parseObject(config, NodeConfigModel.class);
            }
        } catch (Exception e) {
            System.out.println("无法读取配置文件。默认配置文件路径为config/node_config.json\nconfig模板如下：\n" +
                    "类型        必填性         参数名称          参数解释\n" +
                    "String     must          rpcAddress       节点rpc地址\n" +
                    "String     must          hrp              hrp值\n" +
                    "Long       must          chainId          节点所在的链id\n");
            throw new RuntimeException("配置文件不正确");
        }
    }
}
