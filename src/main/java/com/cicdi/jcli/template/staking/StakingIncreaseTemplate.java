package com.cicdi.jcli.template.staking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * "type": 0,
 * "nodeId": "",自动读取当前工作目录下nodeId
 * "amount": 0.00
 *
 * @author haypo
 * @date 2021/1/7
 */
@Data
public class StakingIncreaseTemplate {
    @JsonProperty(required = true)
    private BigInteger type;
    @JsonProperty(required = true)
    private String nodeId;
    @JsonProperty(required = true)
    private BigDecimal amount;
}
