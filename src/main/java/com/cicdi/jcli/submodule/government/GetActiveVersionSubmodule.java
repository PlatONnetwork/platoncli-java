package com.cicdi.jcli.submodule.government;

import com.alaya.contracts.ppos.dto.CallResponse;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ConfigUtil;

import java.math.BigInteger;

/**
 * 查询节点的链生效版本
 *
 * @author haypo
 * @date 2021/1/17
 */
@Parameters(commandNames = "government_getActiveVersion", commandDescription = "查询节点的链生效版本")
public class GetActiveVersionSubmodule extends AbstractSimpleSubmodule {
    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        CallResponse<BigInteger> callResponse = ProposalContractX.load(createWeb3j(nodeConfigModel), nodeConfigModel.getHrp()).getActiveVersion().send();
        if (callResponse.isStatusOk()) {
            return "Version: " + callResponse.getData().toString();
        }
        return Common.FAIL_STR + ": " + callResponse.getErrMsg();
    }
}
