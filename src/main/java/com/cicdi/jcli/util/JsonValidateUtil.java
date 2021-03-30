package com.cicdi.jcli.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author haypo
 * @date 2021/3/30
 */
@Slf4j
public class JsonValidateUtil {
    /**
     * 校验JSON
     *
     * @param schema   json模式数据（可以理解为校验模板）
     * @param instance 需要验证Json数据
     * @return 校验结果
     */
    public static ProcessingReport validatorJsonSchema(String schema, String instance) throws IOException {
        ProcessingReport processingReport;

        JsonNode jsonSchema = JsonLoader.fromString(schema);
        JsonNode jsonData = JsonLoader.fromString(instance);
        processingReport = JsonSchemaFactory.byDefault().getValidator().validateUnchecked(jsonSchema, jsonData);
        boolean success = processingReport.isSuccess();
        if (!success) {
            for (ProcessingMessage processingMessage : processingReport) {
                log.error(String.valueOf(processingMessage));
            }
        }
        return processingReport;
    }
}
