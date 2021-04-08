package com.cicdi.jcli.submodule.government;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.template.government.GetGovernParamValueTemplate;
import com.cicdi.jcli.util.CallResponseUtil;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.ParamUtil;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.protocol.Web3j;

/**
 * 查询最新的治理参数值
 *
 * @author haypo
 * @date 2021/1/17
 */
@Parameters(commandNames = "government_getGovernParamValue", resourceBundle = "command", commandDescription = "查询最新的治理参数值")
public class GetGovernParamValueSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--template", "-template", "-t"}, help = true, description = "查看委托交易参数模板，与其他参数共存没有效果，单独执行查看")
    protected boolean template;
    @Parameter(names = {"--param", "-param", "-p"}, description = "交易参数json字符串，或者交易参数json文件路径", required = true)
    protected String param;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        if (template && argv.length == Common.TWO) {
            return '\n' +
                    "类型            必填性         参数名称            参数解释\n" +
                    "String          must          module             模块名称，可以通过government_listGovernParam查询得到\n" +
                    "String          must          name               参数名称，可以通过government_listGovernParam查询得到\n";
        }
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        ProposalContractX proposalContractX = ProposalContractX.load(web3j, nodeConfigModel.getHrp());
        GetGovernParamValueTemplate template = ParamUtil.readParam(param, GetGovernParamValueTemplate.class);
        CallResponse<String> callResponse = proposalContractX.getGovernParamValue(template.getModule(), template.getName()).send();
        return CallResponseUtil.handleCallResponse(callResponse);
    }
}
