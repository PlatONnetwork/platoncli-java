package com.cicdi.jcli.submodule.staking;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.staking.StakingCreateTemplate;
import com.cicdi.jcli.util.contract.staking.StakingCreateUtil;


/**
 * 创建验证人
 *
 * @author haypo
 * @date 2020/12/25
 */
@Parameters(commandNames = "staking_create", commandDescription = "创建验证人")
public class StakingCreateSubmodule extends AbstractComplexSubmodule<StakingCreateTemplate, StakingCreateUtil> {

    @Override
    public FunctionUtil<StakingCreateUtil> function() {
        return () -> new StakingCreateUtil(isOnline(), address, config, param, StakingCreateTemplate.class);
    }

    @Override
    public String generateTemplate() {
        return "类型             必填性         参数名称             参数解释\n" +
                "BigInteger      must          type               金额类型，0: 自由金额，1: 锁仓金额\n" +
                "String          must          benefitAddress     收益账户\n" +
                "String          optional      nodeId             节点ID\n" +
                "String          optional      externalId         外部ID\n" +
                "String          optional      nodeName           节点名字\n" +
                "String          optional      website            节点的第三方主页\n" +
                "String          optional      details            节点的描述\n" +
                "BigDecimal      must          amount             质押的金额，以hrp（atp、lat）为单位\n" +
                "BigInteger      must          delegateRewardPer  从佣金中获得的奖励份额的比例\n";
    }
}
