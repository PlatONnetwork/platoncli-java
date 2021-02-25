package com.cicdi.jcli.submodule.query;

import com.alaya.contracts.ppos.dto.CallResponse;
import com.alaya.protocol.Web3j;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.StakingContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.ConvertUtil;

import java.math.BigInteger;

/**
 * 查询当前结算周期的区块奖励
 *
 * @author haypo
 * @date 2021/1/18
 */
@Parameters(commandNames = "query_getPackageReward", commandDescription = "查询当前结算周期的区块奖励")
public class GetPackageRewardSubmodule extends AbstractSimpleSubmodule {
    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        CallResponse<BigInteger> callResponse = StakingContractX.load(web3j, nodeConfigModel.getHrp()).getPackageReward().send();
        if (callResponse.isStatusOk()) {
            return "Reward:" + ConvertUtil.von2Hrp(callResponse.getData()) + nodeConfigModel.getHrp();
        } else {
            return genFailString(callResponse);
        }
    }
}