package com.cicdi.jcli.template.delegate;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * "stakingBlockNum": 0,
 * "nodeId": "",
 * "amount": 0.00
 *
 * @author haypo
 * @date 2021/1/8
 */
@Data
public class DelegateUnDelegateTemplate {
    private BigInteger stakingBlockNum;
    private String nodeId;
    private BigDecimal amount;
}
