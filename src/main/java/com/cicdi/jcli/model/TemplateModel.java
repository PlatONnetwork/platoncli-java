package com.cicdi.jcli.model;

import lombok.Data;

@Data
public class TemplateModel {
    private String type;
    private String required;
    private String paramName;
    private String desc;
}
