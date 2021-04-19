package com.cicdi.jcli.submodule.staking;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.staking.StakingUpdateTemplate;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ResourceBundleUtil;
import com.cicdi.jcli.util.contract.staking.StakingUpdateUtil;

/**
 * 修改质押信息
 *
 * @author haypo
 * @date 2020/12/29
 */
@Parameters(commandNames = "staking_update", resourceBundle = "command", commandDescriptionKey = "staking.update")
public class StakingUpdateSubmodule extends AbstractComplexSubmodule<StakingUpdateTemplate, StakingUpdateUtil> {

    @Override
    public FunctionUtil<StakingUpdateUtil> function() {
        return () -> new StakingUpdateUtil(isOnline(), address, config, param, StakingUpdateTemplate.class);
    }

    @Override
    public String generateTemplate() {
        ResourceBundleUtil.printTemplate("StakingUpdateSubmodule");
        return Common.SUCCESS_STR;
    }

}
