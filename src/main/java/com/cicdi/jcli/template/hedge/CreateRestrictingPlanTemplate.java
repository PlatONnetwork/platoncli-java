package com.cicdi.jcli.template.hedge;

import com.platon.contracts.ppos.abi.CustomType;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.rlp.solidity.RlpType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.cicdi.jcli.util.ConvertUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author haypo
 * @date 2021/3/15
 */
@Data
public class CreateRestrictingPlanTemplate {
    private String account;
    private List<Plan> plans;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Plan extends CustomType {
        private BigInteger epoch;
        private BigDecimal amount;

        @Override
        public RlpType getRlpEncodeData() {
            return new RlpList(RlpString.create(epoch), RlpString.create(ConvertUtil.hrp2Von(amount)));
        }
    }
}

