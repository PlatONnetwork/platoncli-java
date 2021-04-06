package com.cicdi.jcli.submodule.hedge;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.hedge.CreateRestrictingPlanTemplate;
import com.cicdi.jcli.util.contract.hedge.CreateRestrictingPlanUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 创建锁仓计划
 *
 * @author haypo
 * @date 2021/3/15
 */
@Slf4j
@SuppressWarnings("unused")
@Parameters(commandNames = "hedge_createRestrictingPlan", commandDescription = "创建锁仓计划")
public class CreateRestrictingPlanSubmodule extends AbstractComplexSubmodule<CreateRestrictingPlanTemplate, CreateRestrictingPlanUtil> {
    @Override
    public FunctionUtil<CreateRestrictingPlanUtil> function() {
        return () -> new CreateRestrictingPlanUtil(isOnline(), address, config, param, CreateRestrictingPlanTemplate.class);
    }

    @Override
    public String generateTemplate() {
        return "类型            必填性        参数名称          参数解释\n" +
                "String         must          account        锁仓计划释放到的仓库\n" +
                "Array          must          plans          锁仓计划列表(数组)\n" +
                "Plans: \n" +
                "类型             必填性         参数名称          参数解释\n" +
                "BigInteger      must          epoch            表示结算周期的倍数\n" +
                "BigInteger      must          amount           表示目标区块上待释放的金额，单位为von\n";
    }

}
