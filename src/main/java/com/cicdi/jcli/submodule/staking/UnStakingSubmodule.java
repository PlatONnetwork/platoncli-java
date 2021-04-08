package com.cicdi.jcli.submodule.staking;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.staking.StakingUnStakingTemplate;
import com.cicdi.jcli.util.contract.staking.StakingUnStakingUtil;

/**
 * 退出验证人
 *
 * @author haypo
 * @date 2020/12/25
 */
@Parameters(commandNames = "staking_unStaking", resourceBundle = "command", commandDescription = "退出验证人")
public class UnStakingSubmodule extends AbstractComplexSubmodule<StakingUnStakingTemplate, StakingUnStakingUtil> {

    @Override
    public FunctionUtil<StakingUnStakingUtil> function() {
        return () -> new StakingUnStakingUtil(isOnline(), address, config, param, StakingUnStakingTemplate.class);
    }

    @Override
    public String generateTemplate() {
        return "类型         必填性         参数名称          参数解释\n" +
                "String       must         nodeId           节点id，若为空则默认使用config配置的节点id\n";
    }
}
