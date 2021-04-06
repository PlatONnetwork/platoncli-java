package com.cicdi.jcli.submodule.government;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.government.ReportDoubleSignTemplate;
import com.cicdi.jcli.util.contract.government.ReportDoubleSignUtil;

/**
 * 举报双签
 *
 * @author haypo
 * @date 2021/1/17
 */
@Parameters(commandNames = "government_reportDoubleSign", commandDescription = "举报双签")
public class ReportDoubleSignSubmodule extends AbstractComplexSubmodule<ReportDoubleSignTemplate, ReportDoubleSignUtil> {
    @Override
    public FunctionUtil<ReportDoubleSignUtil> function() {
        return () -> new ReportDoubleSignUtil(isOnline(), address, config, param, ReportDoubleSignTemplate.class);
    }

    @Override
    public String generateTemplate() {
        return "类型             必填性         参数名称          参数解释\n" +
                "BigInteger       must          type            代表双签类型，1：prepareBlock，2：prepareVote，3：viewChange\n" +
                "String           must          data            单个证据的json值，格式参照RPC接口Evidences\n";
    }
}
