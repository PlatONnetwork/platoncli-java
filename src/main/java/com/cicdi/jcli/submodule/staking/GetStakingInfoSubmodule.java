package com.cicdi.jcli.submodule.staking;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.StakingContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.CallResponseUtil;
import com.cicdi.jcli.util.ConfigUtil;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.protocol.Web3j;

/**
 * 获取质押信息
 *
 * @author haypo
 * @date 2021/1/18
 */
@Parameters(commandNames = "staking_getStakingInfo", resourceBundle = "command", commandDescriptionKey = "staking.getStakingInfo")
public class GetStakingInfoSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--nodeId", "-nodeId"}, descriptionKey = "staking.getStakingInfo.nodeId", required = true)
    protected String nodeId;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        CallResponse<Node> callResponse = StakingContractX.load(web3j, nodeConfigModel.getHrp()).getStakingInfo(nodeId).send();
        return CallResponseUtil.handleCallResponse(callResponse);
    }
}
