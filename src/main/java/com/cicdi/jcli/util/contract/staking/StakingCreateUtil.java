package com.cicdi.jcli.util.contract.staking;

import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.dto.enums.StakingAmountType;
import com.alaya.contracts.ppos.dto.req.StakingParam;
import com.alaya.crypto.CipherException;
import com.alaya.protocol.http.HttpService;
import com.cicdi.jcli.contractx.StakingContractX;
import com.cicdi.jcli.template.staking.StakingCreateTemplate;
import com.cicdi.jcli.util.ConvertUtil;
import com.cicdi.jcli.util.NetworkParametersUtil;
import com.cicdi.jcli.util.Web3jUtil;
import com.cicdi.jcli.util.contract.BaseContractUtil;

import java.io.IOException;

/**
 * @author haypo
 * @date 2021/1/6
 */
public class StakingCreateUtil extends BaseContractUtil<StakingCreateTemplate> {

    public StakingCreateUtil(boolean isOnline, String address, String config, String param, Class<StakingCreateTemplate> clazz) throws IOException, CipherException {
        super(isOnline, address, config, param, clazz);
    }

    @Override
    public Function createFunction() throws IOException {
        String blsProof = web3j.getSchnorrNIZKProve().send().getAdminSchnorrNIZKProve();
        HttpService httpService = new HttpService(nodeConfigModel.getRpcAddress());
        StakingAmountType sta = t.getType().longValue() == 0 ? StakingAmountType.FREE_AMOUNT_TYPE : StakingAmountType.RESTRICTING_AMOUNT_TYPE;
        StakingParam stakingParam = new StakingParam.Builder()
                .setWebSite(String.valueOf(t.getWebsite()))
                .setStakingAmountType(sta)
                .setNodeName(t.getNodeName())
                .setProcessVersion(web3j.getProgramVersion().send().getAdminProgramVersion())
                .setNodeId(t.getNodeId())
                .setExternalId(String.valueOf(t.getExternalId()))
                .setDetails(String.valueOf(t.getDetails()))
                .setRewardPer(t.getDelegateRewardPer())
                .setAmount(ConvertUtil.hrp2Von(t.getAmount()))
                .setBlsProof(blsProof)
                .setBlsPubKey(Web3jUtil.getBlsPubKey(httpService))
                .setBenifitAddress(t.getBenefitAddress())
                .build();
        return StakingContractX.createStakingFunction(stakingParam);

    }

    @Override
    public String getPposContractAddress() {
        return NetworkParametersUtil.getPposContractAddressOfStaking(nodeConfigModel.getHrp());
    }
}
