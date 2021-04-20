package com.cicdi.jcli.template.staking;

import com.platon.contracts.ppos.dto.enums.StakingAmountType;
import lombok.Data;

import java.math.BigDecimal;

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
    private StakingAmountType type;
    private String nodeId;
    private BigDecimal amount;
}
