package com.cicdi.jcli.submodule.delegate;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.NodeContractX;
import com.cicdi.jcli.contractx.RewardContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.template.delegate.GetDelegateRewardTemplate;
import com.cicdi.jcli.util.*;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.contracts.ppos.dto.resp.Reward;
import com.platon.protocol.Web3j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询账户在各节点未提取委托奖励
 *
 * @author haypo
 * @date 2021/1/8
 */
@Parameters(commandNames = "delegate_getDelegateReward", resourceBundle = "command", commandDescriptionKey = "delegate.getDelegateReward")
public class GetDelegateRewardSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--param", "-param", "-p"}, descriptionKey = "param", required = true)
    protected String param;
    @Parameter(names = {"--template", "-template", "-t"}, help = true, descriptionKey = "template")
    protected boolean template;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        if (template && argv.length == Common.TWO) {
            return ResourceBundleUtil.getTemplateString("GetDelegateRewardSubmodule");
        }
        //校验json
        GetDelegateRewardTemplate getDelegateRewardTemplate = ParamUtil.readParam(param, GetDelegateRewardTemplate.class,
                JsonUtil.readJsonSchemaFromResource("/json/GetDelegateRewardTemplateSchema.json"));

        if (getDelegateRewardTemplate.getNodeIds() == null || getDelegateRewardTemplate.getNodeIds().isEmpty()) {
            List<Node> nodes = NodeContractX.load(web3j, nodeConfigModel.getHrp()).getCandidateList().send().getData();
            getDelegateRewardTemplate.setNodeIds(nodes.stream().map(Node::getNodeId).collect(Collectors.toList()));
        }
        RewardContractX rcx = RewardContractX.load(web3j, nodeConfigModel.getHrp());
        CallResponse<List<Reward>> response = rcx.getDelegateReward(getDelegateRewardTemplate.getAddress(),
                getDelegateRewardTemplate.getNodeIds()).send();

        return CallResponseUtil.handleCallResponse(response);
    }
}
