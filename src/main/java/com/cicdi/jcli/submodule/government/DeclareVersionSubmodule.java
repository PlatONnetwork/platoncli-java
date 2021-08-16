package com.cicdi.jcli.submodule.government;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.government.DeclareVersionTemplate;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ResourceBundleUtil;
import com.cicdi.jcli.util.contract.government.DeclareVersionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ResourceBundle;

/**
 * 版本声明
 *
 * @author haypo
 * @date 2020/12/31
 */
@Parameters(commandNames = "government_declareVersion", resourceBundle = "command", commandDescriptionKey = "government.declareVersion")
public class DeclareVersionSubmodule extends AbstractComplexSubmodule<DeclareVersionTemplate, DeclareVersionUtil> {
    @Override
    public FunctionUtil<DeclareVersionUtil> function() {
        return () -> new DeclareVersionUtil(isOnline(), address, config, param, DeclareVersionTemplate.class);
    }

    @Override
    public String generateTemplate() {
        ResourceBundleUtil.printTemplate("DeclareVersionSubmodule");
        return Common.SUCCESS_STR;
    }
}
