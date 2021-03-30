package com.cicdi.jcli.util.contract.staking;

import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.enums.StakingAmountType;
import com.platon.crypto.CipherException;
import com.cicdi.jcli.contractx.StakingContractX;
import com.cicdi.jcli.template.staking.StakingIncreaseTemplate;
import com.cicdi.jcli.util.ConvertUtil;
import com.cicdi.jcli.util.NetworkParametersUtil;
import com.cicdi.jcli.util.StringUtil;
import com.cicdi.jcli.util.Web3jUtil;
import com.cicdi.jcli.util.contract.BaseContractUtil;

import java.io.IOException;

/**
 * @author haypo
 * @date 2021/1/7
 */
public class StakingIncreaseUtil extends BaseContractUtil<StakingIncreaseTemplate> {
    public StakingIncreaseUtil(boolean isOnline, String address, String config, String param, Class<StakingIncreaseTemplate> clazz) throws IOException, CipherException {
        super(isOnline, address, config, param, clazz);
    }

    @Override
    public Function createFunction() throws IOException {
        StakingAmountType sta = t.getType().longValue() == 0 ? StakingAmountType.FREE_AMOUNT_TYPE : StakingAmountType.RESTRICTING_AMOUNT_TYPE;
        if (StringUtil.isBlank(t.getNodeId())) {
            t.setNodeId(Web3jUtil.getNodeId(nodeConfigModel.getRpcAddress()));
        }
        return StakingContractX.createAddStakingFunction(
                t.getNodeId(),
                sta,
                ConvertUtil.hrp2Von(t.getAmount())
        );
    }

    @Override
    public String getPposContractAddress() {
        return NetworkParametersUtil.getPposContractAddressOfStaking(nodeConfigModel.getHrp());
    }
}
