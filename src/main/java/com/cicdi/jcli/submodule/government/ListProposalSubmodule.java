package com.cicdi.jcli.submodule.government;

import com.alaya.contracts.ppos.dto.CallResponse;
import com.alaya.contracts.ppos.dto.resp.Proposal;
import com.alaya.protocol.Web3j;
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
@Parameters(commandNames = "government_listProposal", commandDescription = "查询提案列表")
public class ListProposalSubmodule extends AbstractSimpleSubmodule {
    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        ProposalContractX pcx = ProposalContractX.load(web3j, nodeConfigModel.getHrp());
        CallResponse<List<Proposal>> callResponse = pcx.getProposalList().send();
        if (callResponse.isStatusOk()) {
            return JsonUtil.toPrettyJsonString(callResponse.getData());
        }
        return Common.FAIL_STR;
    }
}
