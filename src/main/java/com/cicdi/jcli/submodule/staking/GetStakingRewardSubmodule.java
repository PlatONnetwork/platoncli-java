package com.cicdi.jcli.submodule.staking;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.NodeContractX;
import com.cicdi.jcli.contractx.StakingContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.ConvertUtil;
import com.platon.protocol.Web3j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * 查询当前结算周期的质押奖励
 *
 * @author haypo
 * @date 2021/1/8
 */
@Parameters(commandNames = "staking_getStakingReward", resourceBundle = "command", commandDescriptionKey = "staking.getStakingReward")
public class GetStakingRewardSubmodule extends AbstractSimpleSubmodule {

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel =  ConfigUtil.readConfig(config);
        FastHttpService fastHttpService = new FastHttpService(nodeConfigModel.getRpcAddress());
        Web3j web3j = Web3j.build(fastHttpService);
        BigInteger reward = StakingContractX.load(web3j, nodeConfigModel.getHrp()).getStakingReward().send().getData();
        int verifierNum = NodeContractX.load(web3j, nodeConfigModel.getHrp()).getVerifierList().send().getData().size();
        return ConvertUtil.von2Hrp(reward).divide(BigDecimal.valueOf(verifierNum), MathContext.DECIMAL64) + nodeConfigModel.getHrp();
    }

}
