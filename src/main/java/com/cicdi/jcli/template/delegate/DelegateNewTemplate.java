package com.cicdi.jcli.template.delegate;

import com.platon.contracts.ppos.dto.enums.StakingAmountType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author haypo
 * @date 2021/1/4
 */
@Data
public class DelegateNewTemplate {
    private StakingAmountType type;
    private String nodeId;
    private BigDecimal amount;
}
