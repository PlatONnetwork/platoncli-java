package com.cicdi.jcli.submodule.government;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.government.VoteTemplate;
import com.cicdi.jcli.util.ResourceBundleUtil;
import com.cicdi.jcli.util.contract.government.VoteUtil;

/**
 * 提案投票
 *
 * @author haypo
 * @date 2021/1/14
 */
@Parameters(commandNames = "government_vote",resourceBundle = "command",  commandDescription = "提案投票")
public class VoteSubmodule extends AbstractComplexSubmodule<VoteTemplate, VoteUtil> {
    @Override
    public FunctionUtil<VoteUtil> function() {
        return () -> new VoteUtil(isOnline(), address, config, param, VoteTemplate.class);
    }

    @Override
    public String generateTemplate() {
        return ResourceBundleUtil.getTemplateString("VoteSubmodule");
    }
}
