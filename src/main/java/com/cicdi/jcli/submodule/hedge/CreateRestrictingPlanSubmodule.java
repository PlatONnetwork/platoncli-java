package com.cicdi.jcli.submodule.hedge;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.hedge.CreateRestrictingPlanTemplate;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ResourceBundleUtil;
import com.cicdi.jcli.util.contract.hedge.CreateRestrictingPlanUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 创建锁仓计划
 *
 * @author haypo
 * @date 2021/3/15
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "hedge_createRestrictingPlan", resourceBundle = "command", commandDescriptionKey = "hedge.createRestrictingPlan")
public class CreateRestrictingPlanSubmodule extends AbstractComplexSubmodule<CreateRestrictingPlanTemplate, CreateRestrictingPlanUtil> {
    @Override
    public FunctionUtil<CreateRestrictingPlanUtil> function() {
        return () -> new CreateRestrictingPlanUtil(isOnline(), address, config, param, CreateRestrictingPlanTemplate.class);
    }

    @Override
    public String generateTemplate() {
        ResourceBundleUtil.printTemplate("CreateRestrictingPlanSubmodule");
        System.out.println("plans:");
        ResourceBundleUtil.printTemplate("plans");
        return Common.SUCCESS_STR;
    }

}
