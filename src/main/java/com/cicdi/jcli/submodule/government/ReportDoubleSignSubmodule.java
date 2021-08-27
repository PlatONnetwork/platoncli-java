package com.cicdi.jcli.submodule.government;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.government.ReportDoubleSignTemplate;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ResourceBundleUtil;
import com.cicdi.jcli.util.contract.government.ReportDoubleSignUtil;

/**
 * 举报双签
 *
 * @author haypo
 * @date 2021/1/17
 */
@Parameters(commandNames = "government_reportDoubleSign", resourceBundle = "command", commandDescriptionKey = "government.reportDoubleSign")
public class ReportDoubleSignSubmodule extends AbstractComplexSubmodule<ReportDoubleSignTemplate, ReportDoubleSignUtil> {
    @Override
    public FunctionUtil<ReportDoubleSignUtil> function() {
        return () -> new ReportDoubleSignUtil(isOnline(), address, config, param, ReportDoubleSignTemplate.class);
    }

    @Override
    public String generateTemplate() {
        ResourceBundleUtil.printTemplate("ReportDoubleSignSubmodule");
        return Common.SUCCESS_STR;
    }
}
