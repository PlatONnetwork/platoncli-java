package com.cicdi.jcli.submodule.query;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.StakingContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.CallResponseUtil;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.ConvertUtil;
import com.cicdi.jcli.util.ResourceBundleUtil;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.protocol.Web3j;

import java.math.BigInteger;

/**
 * 查询当前结算周期的区块奖励
 *
 * @author haypo
 * @date 2021/1/18
 */
@Parameters(commandNames = "query_getPackageReward", resourceBundle = "command", commandDescriptionKey = "query.getPackageReward")
public class GetPackageRewardSubmodule extends AbstractSimpleSubmodule {
    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        CallResponse<BigInteger> callResponse = StakingContractX.load(web3j, nodeConfigModel.getHrp()).getPackageReward().send();
        return CallResponseUtil.handleCallResponse(callResponse);
    }
}
