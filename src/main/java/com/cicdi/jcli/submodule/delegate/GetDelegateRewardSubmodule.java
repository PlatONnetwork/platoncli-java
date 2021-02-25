package com.cicdi.jcli.submodule.delegate;

import com.alaya.contracts.ppos.dto.CallResponse;
import com.alaya.contracts.ppos.dto.common.ErrorCode;
import com.alaya.contracts.ppos.dto.resp.Node;
import com.alaya.contracts.ppos.dto.resp.Reward;
import com.alaya.protocol.Web3j;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.NodeContractX;
import com.cicdi.jcli.contractx.RewardContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.template.delegate.GetDelegateRewardTemplate;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;
import com.cicdi.jcli.util.ParamUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询账户在各节点未提取委托奖励
 *
 * @author haypo
 * @date 2021/1/8
 */
@Parameters(commandNames = "delegate_getDelegateReward", commandDescription = "查询账户在各节点未提取委托奖励")
public class GetDelegateRewardSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--param", "-param", "-p"}, description = "交易参数json字符串，或者交易参数json文件路径", required = true)
    protected String param;
    @Parameter(names = {"--template", "-template", "-t"}, help = true, description = "查看委托交易参数模板，与其他参数共存没有效果，单独执行查看")
    protected boolean template;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        if (template && argv.length == Common.TWO) {
            return '\n' +
                    "类型             必填性         参数名称          参数解释\n" +
                    "String          must          address          账户地址\n" +
                    "List<String>    must          nodeIds          节点ID列表，若为空则表示查询当前全网节点\n";
        }
        GetDelegateRewardTemplate getDelegateRewardTemplate = ParamUtil.readParam(param, GetDelegateRewardTemplate.class);
        if (getDelegateRewardTemplate.getNodeIds() == null || getDelegateRewardTemplate.getNodeIds().isEmpty()) {
            List<Node> nodes = NodeContractX.load(web3j, nodeConfigModel.getHrp()).getCandidateList().send().getData();
            getDelegateRewardTemplate.setNodeIds(nodes.stream().map(Node::getNodeId).collect(Collectors.toList()));
        }
        RewardContractX rcx = RewardContractX.load(web3j, nodeConfigModel.getHrp());
        CallResponse<List<Reward>> response = rcx.getDelegateReward(getDelegateRewardTemplate.getAddress(),
                getDelegateRewardTemplate.getNodeIds()).send();

        return response.isStatusOk() ?
                "Data:\n" + JsonUtil.toPrettyJsonString(response.getData()) : Common.FAIL_STR + ":" + ErrorCode.getErrorMsg(response.getCode());
    }
}
