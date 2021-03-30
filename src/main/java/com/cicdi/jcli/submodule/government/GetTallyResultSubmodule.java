package com.cicdi.jcli.submodule.government;

import com.platon.contracts.ppos.dto.resp.TallyResult;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;

/**
 * 查询提案结果
 *
 * @author haypo
 * @date 2021/2/19
 */
@Parameters(commandNames = "government_getTallyResult", commandDescription = "查询提案结果")
public class GetTallyResultSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--pid", "-pid"}, description = "提案id")
    protected String pid;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        ProposalContractX pcx = ProposalContractX.load(createWeb3j(), nodeConfigModel.getHrp());
        TallyResult tallyResult = pcx.getTallyResult(pid).send().getData();
        return JsonUtil.toPrettyJsonString(tallyResult);
    }
}
