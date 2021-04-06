package com.cicdi.jcli.util;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;

/**
 * @author haypo
 * @date 2021/1/6
 */
public class ParamUtil {
    /**
     * 根据param反序列化，并使用schema校验
     *
     * @param param  json文件或者json字符串
     * @param clazz  模板类型
     * @param schema 模板校验器
     * @param <T>    类型泛型
     * @return 模板实例化对象
     * @throws IOException json读取异常
     */
    public static <T> T readParam(String param, Class<T> clazz, String schema) throws IOException {
        File f = new File(param);
        if (f.exists() && f.isFile()) {
            return JsonUtil.readFile(f, clazz, schema);
        } else {
            //param is json string
            if (!StringUtil.isBlank(schema)) {
                if (!JsonValidateUtil.validatorJsonSchema(schema, param).isSuccess()) {
                    throw new RuntimeException("json schema not match");
                }
            }
            return JSON.parseObject(param, clazz);
        }
    }

    /**
     * 根据param反序列化，无schema校验
     *
     * @param param 可以为json文件路径，也可以为json字符串
     * @param clazz 反序列化的类
     * @param <T>   反序列化的泛型
     * @return 反序列化的对象
     */
    public static <T> T readParam(String param, Class<T> clazz) throws IOException {
        File f = new File(param);
        if (f.exists() && f.isFile()) {
            return JsonUtil.readFile(f, clazz, null);
        } else {
            return JSON.parseObject(param, clazz);
        }
    }
}
