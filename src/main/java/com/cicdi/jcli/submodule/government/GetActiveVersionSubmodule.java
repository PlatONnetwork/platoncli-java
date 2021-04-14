package com.cicdi.jcli.submodule.government;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.CallResponseUtil;
import com.cicdi.jcli.util.ConfigUtil;
import com.platon.contracts.ppos.dto.CallResponse;

import java.math.BigInteger;

/**
 * 查询节点的链生效版本
 *
 * @author haypo
 * @date 2021/1/17
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "government_getActiveVersion", resourceBundle = "command", commandDescriptionKey = "government.getActiveVersion")
public class GetActiveVersionSubmodule extends AbstractSimpleSubmodule {
    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        CallResponse<BigInteger> callResponse = ProposalContractX.load(createWeb3j(nodeConfigModel), nodeConfigModel.getHrp()).getActiveVersion().send();
        return CallResponseUtil.handleCallResponse(callResponse);
    }
}
