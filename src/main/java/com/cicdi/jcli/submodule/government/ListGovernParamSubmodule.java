package com.cicdi.jcli.submodule.government;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.CallResponseUtil;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.resp.GovernParam;
import com.platon.protocol.Web3j;

import java.util.List;


/**
 * 查询治理参数列表
 *
 * @author haypo
 * @date 2021/1/17
 */
@Parameters(commandNames = "government_listGovernParam", resourceBundle = "command", commandDescriptionKey = "government.listGovernParam")
public class ListGovernParamSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--module", "-module", "-m"}, descriptionKey = "government.listGovernParam.module")
    protected GovernParamModule module;

    enum GovernParamModule {
        /**
         *
         */
        staking, slashing, block, reward, restricting
    }

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        ProposalContractX pcx = ProposalContractX.load(web3j, nodeConfigModel.getHrp());
        CallResponse<List<GovernParam>> response = pcx.getParamList(module == null ? "" : module.name()).send();
        return CallResponseUtil.handleCallResponse(response);
    }
}
