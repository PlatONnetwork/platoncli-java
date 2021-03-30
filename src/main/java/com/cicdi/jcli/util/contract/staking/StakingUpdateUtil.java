package com.cicdi.jcli.util.contract.staking;

import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.req.UpdateStakingParam;
import com.platon.crypto.CipherException;
import com.cicdi.jcli.contractx.StakingContractX;
import com.cicdi.jcli.template.staking.StakingUpdateTemplate;
import com.cicdi.jcli.util.NetworkParametersUtil;
import com.cicdi.jcli.util.StringUtil;
import com.cicdi.jcli.util.Web3jUtil;
import com.cicdi.jcli.util.contract.BaseContractUtil;

import java.io.IOException;

/**
 * @author haypo
 * @date 2021/1/7
 */
public class StakingUpdateUtil extends BaseContractUtil<StakingUpdateTemplate> {

    public StakingUpdateUtil(boolean isOnline, String address, String config, String param, Class<StakingUpdateTemplate> clazz) throws IOException, CipherException {
        super(isOnline, address, config, param, clazz);
    }

    @Override
    public Function createFunction() throws IOException {
        if (StringUtil.isBlank(t.getNodeId())) {
            t.setNodeId(Web3jUtil.getNodeId(nodeConfigModel.getRpcAddress()));
        }
        UpdateStakingParam updateStakingParam = new UpdateStakingParam.Builder()
                .setWebSite(t.getWebsite())
                .setNodeName(t.getNodeName())
                .setNodeId(t.getNodeId())
                .setExternalId(t.getExternalId())
                .setDetails(t.getDetails())
                .setRewardPer(t.getDelegateRewardPer())
                .setBenifitAddress(t.getBenefitAddress())
                .build();
        return StakingContractX.createUpdateStakingFunction(updateStakingParam);
    }

    @Override
    public String getPposContractAddress() {
        return NetworkParametersUtil.getPposContractAddressOfStaking(nodeConfigModel.getHrp());
    }
}
