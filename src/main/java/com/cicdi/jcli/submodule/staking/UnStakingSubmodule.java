package com.cicdi.jcli.submodule.staking;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.staking.StakingUnStakingTemplate;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ResourceBundleUtil;
import com.cicdi.jcli.util.contract.staking.StakingUnStakingUtil;

/**
 * 退出验证人
 *
 * @author haypo
 * @date 2020/12/25
 */
@Parameters(commandNames = "staking_unStaking", resourceBundle = "command", commandDescriptionKey = "staking.unStaking")
public class UnStakingSubmodule extends AbstractComplexSubmodule<StakingUnStakingTemplate, StakingUnStakingUtil> {

    @Override
    public FunctionUtil<StakingUnStakingUtil> function() {
        return () -> new StakingUnStakingUtil(isOnline(), address, config, param, StakingUnStakingTemplate.class);
    }

    @Override
    public String generateTemplate() {
        ResourceBundleUtil.printTemplate("UnStakingSubmodule");
        return Common.SUCCESS_STR;
    }
}
