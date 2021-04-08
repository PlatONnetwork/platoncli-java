package com.cicdi.jcli.submodule.government;

import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.resp.GovernParam;
import com.platon.protocol.Web3j;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;

import java.util.List;

/**
 * 查询治理参数列表
 *
 * @author haypo
 * @date 2021/1/17
 */
@Parameters(commandNames = "government_listGovernParam",resourceBundle = "command",  commandDescription = "查询治理参数列表")
public class ListGovernParamSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--module", "-module", "-m"}, description = "查询具体的参数模块，输入\"\"空串，表示查询所有治理参数。包含staking、slashing、block、reward、restricting等模块")
    protected String module = "";

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        ProposalContractX pcx = ProposalContractX.load(web3j, nodeConfigModel.getHrp());
        CallResponse<List<GovernParam>> response = pcx.getParamList(module).send();
        if (response.isStatusOk()) {
            return JsonUtil.toPrettyJsonString(response.getData());
        } else {
            return genFailString(response);
        }
    }
}
