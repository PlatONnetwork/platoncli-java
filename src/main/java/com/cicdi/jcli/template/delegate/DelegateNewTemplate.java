package com.cicdi.jcli.template.delegate;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author haypo
 * @date 2021/1/4
 */
@Data
public class DelegateNewTemplate {
    private Integer type;
    private String nodeId;
    private BigDecimal amount;
}
