package com.cicdi.jcli.submodule.government;

import com.cicdi.jcli.util.CallResponseUtil;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.resp.Proposal;
import com.platon.protocol.Web3j;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;

import java.util.List;

/**
 * 查询提案列表
 *
 * @author haypo
 * @date 2021/1/17
 */
@Parameters(commandNames = "government_listProposal", resourceBundle = "command", commandDescriptionKey = "government.listProposal")
public class ListProposalSubmodule extends AbstractSimpleSubmodule {
    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        ProposalContractX pcx = ProposalContractX.load(web3j, nodeConfigModel.getHrp());
        CallResponse<List<Proposal>> response = pcx.getProposalList().send();
        return CallResponseUtil.handleCallResponse(response);
    }
}
