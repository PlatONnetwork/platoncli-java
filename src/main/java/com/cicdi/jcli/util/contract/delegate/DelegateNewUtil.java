package com.cicdi.jcli.util.contract.delegate;

import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.dto.enums.StakingAmountType;
import com.alaya.crypto.CipherException;
import com.cicdi.jcli.contractx.DelegateContractX;
import com.cicdi.jcli.template.delegate.DelegateNewTemplate;
import com.cicdi.jcli.util.ConvertUtil;
import com.cicdi.jcli.util.NetworkParametersUtil;
import com.cicdi.jcli.util.StringUtil;
import com.cicdi.jcli.util.Web3jUtil;
import com.cicdi.jcli.util.contract.BaseContractUtil;

import java.io.IOException;

/**
 * @author haypo
 * @date 2021/1/4
 */
public class DelegateNewUtil extends BaseContractUtil<DelegateNewTemplate> {

    public DelegateNewUtil(boolean isOnline, String address, String config, String param, Class<DelegateNewTemplate> clazz) throws IOException, CipherException {
        super(isOnline, address, config, param, clazz);
    }

    @Override
    public Function createFunction() throws IOException {
        StakingAmountType sta = t.getType().longValue() == 0 ? StakingAmountType.FREE_AMOUNT_TYPE : StakingAmountType.RESTRICTING_AMOUNT_TYPE;
        if (StringUtil.isBlank(t.getNodeId())) {
            t.setNodeId(Web3jUtil.getNodeId(nodeConfigModel.getRpcAddress()));
        }
        return DelegateContractX.createDelegateFunction(
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
