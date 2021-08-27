package com.cicdi.jcli.submodule.delegate;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.delegate.DelegateNewTemplate;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ResourceBundleUtil;
import com.cicdi.jcli.util.contract.delegate.DelegateNewUtil;

/**
 * 委托
 *
 * @author haypo
 * @date 2021/1/4
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "delegate_new", resourceBundle = "command", commandDescriptionKey = "delegate.new")
public class DelegateNewSubmodule extends AbstractComplexSubmodule<DelegateNewTemplate, DelegateNewUtil> {

    @Override
    public FunctionUtil<DelegateNewUtil> function() {
        return () -> new DelegateNewUtil(isOnline(), address, config, param, DelegateNewTemplate.class);
    }

    @Override
    public String generateTemplate() {
        ResourceBundleUtil.printTemplate("DelegateNewSubmodule");
        return Common.SUCCESS_STR;
    }
}
