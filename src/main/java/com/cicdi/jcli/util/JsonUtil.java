package com.cicdi.jcli.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import lombok.Cleanup;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

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
    public static <T> T readFile(File file, Class<T> type, String schema) throws IOException {
        JSONReader jsonReader = new JSONReader(
                new FileReader(file),
                Feature.AllowUnQuotedFieldNames,
                Feature.AllowSingleQuotes);
        if (!StringUtil.isBlank(schema)) {
            String jsonStr = readFile(file);
            ProcessingReport report = JsonValidateUtil.validatorJsonSchema(schema, jsonStr);
            if (!report.isSuccess()) {
                throw new RuntimeException("json schema not match");
            }
        }

        return jsonReader.readObject(type);
    }

    /**
     * 无冲突地写入json文件
     *
     * @param dir        目录
     * @param filename   文件名
     * @param jsonObject json对象
     * @return json文件名
     * @throws IOException io异常
     */
    public static String writeJsonFileWithNoConflict(File dir, String filename, Object jsonObject) throws IOException {
        if (dir.mkdirs()) {
            System.out.println("已创建" + dir.getName() + "文件夹");
        }

        File file = new File(dir, filename);
        int flag = 0;
        while (file.isFile() && file.exists()) {
            file = new File(dir, filename + "." + flag++);
        }
        JSON.writeJSONString(new FileWriter(file), jsonObject, SerializerFeature.PrettyFormat);
        return file.getName();
    }

    /**
     * 无冲突地写入json文件
     *
     * @param filename   文件名
     * @param jsonObject json对象
     * @return json文件名
     * @throws IOException io异常
     */
    public static String writeJsonFileWithNoConflict(String filename, Object jsonObject) throws IOException {
        File file = new File(filename);
        int flag = 0;
        while (file.isFile() && file.exists()) {
            file = new File(filename + "." + flag++);
        }
        JSON.writeJSONString(new FileWriter(file), jsonObject, SerializerFeature.PrettyFormat);
        return file.getName();
    }

    /**
     * 从文件读取json字符串
     *
     * @param file json文件
     * @return json字符串
     * @throws IOException io异常
     */
    public static String readFile(File file) throws IOException {
        @Cleanup JSONReader jsonReader = new JSONReader(
                new FileReader(file),
                Feature.AllowUnQuotedFieldNames,
                Feature.AllowSingleQuotes);
        return jsonReader.readString();
    }

    /**
     * 从resource读取json schema
     *
     * @param filePath resource下的路径
     * @return json字符串
     */
    public static String readJsonSchemaFromResource(String filePath) {
        if (filePath == null) {
            return null;
        }
        String jsonStr;
        try {
            //打包成jar之后读取resource下的json
            @Cleanup Reader reader = new InputStreamReader(
                    Objects.requireNonNull(JsonUtil.class.getResourceAsStream(filePath)),
                    StandardCharsets.UTF_8
            );
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断str是否是json文件
     *
     * @param str 待判断字符串
     * @return 判断结果
     */
    public static boolean isJsonFile(String str) {
        return str.toLowerCase(Locale.ROOT).endsWith(Common.JSON_SUFFIX) || new File(str).isFile();
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
