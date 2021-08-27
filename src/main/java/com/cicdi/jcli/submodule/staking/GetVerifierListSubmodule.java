package com.cicdi.jcli.submodule.staking;

import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.protocol.Web3j;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.NodeContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;

import java.util.List;

/**
 * 查询当前结算周期的验证人列表
 *
 * @author haypo
 * @date 2020/12/23
 */
@Parameters(commandNames = "staking_getVerifierList", resourceBundle = "command", commandDescriptionKey = "staking.getVerifierList")
public class GetVerifierListSubmodule extends AbstractSimpleSubmodule {

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        FastHttpService fastHttpService = new FastHttpService(nodeConfigModel.getRpcAddress());
        Web3j web3j = Web3j.build(fastHttpService);

        List<Node> verifierList = NodeContractX.load(web3j, nodeConfigModel.getHrp()).getVerifierList().send().getData();
        return JsonUtil.toPrettyJsonString(verifierList);
    }
}
