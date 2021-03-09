package com.cicdi.jcli.submodule.staking;

import com.alaya.contracts.ppos.dto.resp.Node;
import com.alaya.protocol.Web3j;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.NodeContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;
import com.cicdi.jcli.util.StringUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据nodeId查询节点质押信息
 *
 * @author haypo
 * @date 2021/1/8
 */
@Parameters(commandNames = "staking_getCandidateInfo", commandDescription = "根据nodeId查询节点质押信息")
public class GetCandidateInfoSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--nodeId", "-nodeId"}, description = "节点id，若为空，则表示查询所有的节点")
    protected String nodeId;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        FastHttpService fastHttpService = new FastHttpService(nodeConfigModel.getRpcAddress());
        Web3j web3j = Web3j.build(fastHttpService);
        List<Node> validatorList = NodeContractX.load(web3j, nodeConfigModel.getHrp()).getCandidateList().send().getData();
        List<Node> result = validatorList.stream().filter(s ->
                {
                    if (StringUtil.isBlank(nodeId)) {
                        return true;
                    } else {
                        return s.getNodeId().equalsIgnoreCase(nodeId.replace("0x", "")) || s.getNodeId().equalsIgnoreCase(nodeId);
                    }
                }
        ).collect(Collectors.toList());
        return JsonUtil.toPrettyJsonString(result);
    }

}
