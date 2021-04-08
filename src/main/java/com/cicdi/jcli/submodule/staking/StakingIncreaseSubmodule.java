package com.cicdi.jcli.submodule.staking;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.staking.StakingIncreaseTemplate;
import com.cicdi.jcli.util.contract.staking.StakingIncreaseUtil;

/**
 * 增持质押
 *
 * @author haypo
 * @date 2020/12/29
 */
@Parameters(commandNames = "staking_increase", resourceBundle = "command", commandDescription = "增持质押")
public class StakingIncreaseSubmodule extends AbstractComplexSubmodule<StakingIncreaseTemplate, StakingIncreaseUtil> {

    @Override
    public FunctionUtil<StakingIncreaseUtil> function() {
        return () -> new StakingIncreaseUtil(isOnline(), address, config, param, StakingIncreaseTemplate.class);
    }

    @Override
    public String generateTemplate() {
        return "类型             必填性         参数名称            参数解释\n" +
                "BigInteger      must          type               金额类型，0: 自由金额，1: 锁仓金额\n" +
                "String          must          nodeId             节点ID，若为空则使用config参数配置的节点id\n" +
                "BigDecimal      must          amount             质押的金额，以hrp（atp、lat）为单位\n";
    }
}