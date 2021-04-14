package com.cicdi.jcli.submodule.government;

import com.platon.contracts.ppos.dto.resp.Proposal;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;

/**
 * 根据提案id查询提案信息
 *
 * @author haypo
 * @date 2021/2/19
 */
@Parameters(commandNames = "government_getProposal",resourceBundle = "command",  commandDescriptionKey = "government.getProposal")
public class GetProposalSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--pid", "-pid"}, descriptionKey = "pid")
    protected String pid;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        ProposalContractX pcx = ProposalContractX.load(createWeb3j(), nodeConfigModel.getHrp());
        Proposal proposal = pcx.getProposal(pid).send().getData();
        return JsonUtil.toPrettyJsonString(proposal);
    }
}
