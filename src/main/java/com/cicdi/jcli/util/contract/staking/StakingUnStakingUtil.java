package com.cicdi.jcli.util.contract.staking;

import com.platon.contracts.ppos.abi.Function;
import com.platon.crypto.CipherException;
import com.cicdi.jcli.contractx.StakingContractX;
import com.cicdi.jcli.template.staking.StakingUnStakingTemplate;
import com.cicdi.jcli.util.NetworkParametersUtil;
import com.cicdi.jcli.util.StringUtil;
import com.cicdi.jcli.util.Web3jUtil;
import com.cicdi.jcli.util.contract.BaseContractUtil;

import java.io.IOException;

/**
 * @author haypo
 * @date 2021/1/7
 */
public class StakingUnStakingUtil extends BaseContractUtil<StakingUnStakingTemplate> {


    public StakingUnStakingUtil(boolean isOnline, String jsonPath, String config, String param, Class<StakingUnStakingTemplate> clazz) throws IOException, CipherException {
        super(isOnline, jsonPath, config, param, clazz);
    }

    @Override
    public Function createFunction() throws IOException {
        if (StringUtil.isBlank(t.getNodeId())) {
            t.setNodeId(Web3jUtil.getNodeId(nodeConfigModel.getRpcAddress()));
        }
        return StakingContractX.createUnStakingFunction(t.getNodeId());
    }

    @Override
    public String getPposContractAddress() {
        return NetworkParametersUtil.getPposContractAddressOfStaking(nodeConfigModel.getHrp());
    }
}
