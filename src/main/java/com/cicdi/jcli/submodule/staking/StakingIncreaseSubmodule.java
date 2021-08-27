package com.cicdi.jcli.submodule.staking;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.staking.StakingIncreaseTemplate;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ResourceBundleUtil;
import com.cicdi.jcli.util.contract.staking.StakingIncreaseUtil;

/**
 * 增持质押
 *
 * @author haypo
 * @date 2020/12/29
 */
@Parameters(commandNames = "staking_increase", resourceBundle = "command", commandDescriptionKey = "staking.increase")
public class StakingIncreaseSubmodule extends AbstractComplexSubmodule<StakingIncreaseTemplate, StakingIncreaseUtil> {

    @Override
    public FunctionUtil<StakingIncreaseUtil> function() {
        return () -> new StakingIncreaseUtil(isOnline(), address, config, param, StakingIncreaseTemplate.class);
    }

    @Override
    public String generateTemplate() {
        ResourceBundleUtil.printTemplate("StakingIncreaseSubmodule");
        return Common.SUCCESS_STR;
    }
}