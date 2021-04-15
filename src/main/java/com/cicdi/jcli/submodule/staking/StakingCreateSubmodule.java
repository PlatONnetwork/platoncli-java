package com.cicdi.jcli.submodule.staking;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.staking.StakingCreateTemplate;
import com.cicdi.jcli.util.ResourceBundleUtil;
import com.cicdi.jcli.util.contract.staking.StakingCreateUtil;

/**
 * 创建验证人
 *
 * @author haypo
 * @date 2020/12/25
 */
@Parameters(commandNames = "staking_create", resourceBundle = "command", commandDescriptionKey = "staking.create")
public class StakingCreateSubmodule extends AbstractComplexSubmodule<StakingCreateTemplate, StakingCreateUtil> {

    @Override
    public FunctionUtil<StakingCreateUtil> function() {
        return () -> new StakingCreateUtil(isOnline(), address, config, param, StakingCreateTemplate.class);
    }

    @Override
    public String generateTemplate() {
        return ResourceBundleUtil.getTemplateString("StakingCreateSubmodule");
    }
}
