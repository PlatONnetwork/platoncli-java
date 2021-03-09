package com.cicdi.jcli.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Cleanup;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author haypo
 * @date 2020/12/25
 */
public class JsonUtil {
    /**
     * 从文件读取json
     *
     * @param file json文件
     * @param type json类
     * @param <T>  json类型
     * @return json对象
     * @throws IOException io异常
     */
    public static <T> T readFile(File file, Class<T> type) throws IOException {
        @Cleanup JSONReader jsonReader = new JSONReader(
                new FileReader(file),
                Feature.AllowUnQuotedFieldNames,
                Feature.AllowSingleQuotes);
        return jsonReader.readObject(type);
    }

    /**
     * 格式化输出json字符串
     *
     * @param object 待转换的对象
     * @return json字符串
     */
    public static String toPrettyJsonString(Object object) {
        String json = "";
        try {
            json = JSON.toJSONString(object,
                    SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.IgnoreNonFieldGetter,
                    SerializerFeature.IgnoreErrorGetter
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
