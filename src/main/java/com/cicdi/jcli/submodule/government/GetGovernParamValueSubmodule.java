package com.cicdi.jcli.submodule.government;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.template.government.GetGovernParamValueTemplate;
import com.cicdi.jcli.util.*;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.protocol.Web3j;

/**
 * 查询最新的治理参数值
 *
 * @author haypo
 * @date 2021/1/17
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "government_getGovernParamValue", resourceBundle = "command", commandDescriptionKey = "government.getGovernParamValue")
public class GetGovernParamValueSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--template", "-template", "-t"}, help = true, descriptionKey = "template")
    protected boolean template;
    @Parameter(names = {"--param", "-param", "-p"}, descriptionKey = "param", required = true)
    protected String param;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        if (template && argv.length == Common.TWO) {
            ResourceBundleUtil.printTemplate("GetGovernParamValueSubmodule");
            return Common.SUCCESS_STR;
        }
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        ProposalContractX proposalContractX = ProposalContractX.load(web3j, nodeConfigModel.getHrp());
        GetGovernParamValueTemplate template = ParamUtil.readParam(param, GetGovernParamValueTemplate.class,
                JsonUtil.readJsonSchemaFromResource("/json/GetGovernParamValueTemplateSchema.json"));
        CallResponse<String> callResponse = proposalContractX.getGovernParamValue(template.getModule(), template.getName()).send();
        return CallResponseUtil.handleCallResponse(callResponse);
    }
}
