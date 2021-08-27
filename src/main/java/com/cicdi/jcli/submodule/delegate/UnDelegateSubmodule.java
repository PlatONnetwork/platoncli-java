package com.cicdi.jcli.submodule.delegate;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.delegate.DelegateUnDelegateTemplate;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ResourceBundleUtil;
import com.cicdi.jcli.util.contract.delegate.DelegateUnDelegateUtil;

/**
 * 减持/撤销委托
 *
 * @author haypo
 * @date 2021/1/8
 */
@Parameters(commandNames = "delegate_unDelegate", resourceBundle = "command", commandDescriptionKey = "delegate.unDelegate")
public class UnDelegateSubmodule extends AbstractComplexSubmodule<DelegateUnDelegateTemplate, DelegateUnDelegateUtil> {

    @Override
    public FunctionUtil<DelegateUnDelegateUtil> function() {
        return () -> new DelegateUnDelegateUtil(isOnline(), address, config, param, DelegateUnDelegateTemplate.class);
    }

    @Override
    public String generateTemplate() {
        ResourceBundleUtil.printTemplate("UnDelegateSubmodule");
        return Common.SUCCESS_STR;
    }

}
