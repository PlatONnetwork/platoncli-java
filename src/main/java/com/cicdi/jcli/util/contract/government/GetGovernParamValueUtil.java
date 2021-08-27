package com.cicdi.jcli.util.contract.government;

import com.platon.contracts.ppos.abi.Function;
import com.platon.crypto.CipherException;
import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.template.government.GetGovernParamValueTemplate;
import com.cicdi.jcli.util.NetworkParametersUtil;
import com.cicdi.jcli.util.contract.BaseContractUtil;

import java.io.IOException;

/**
 * @author haypo
 * @date 2021/1/17
 */
public class GetGovernParamValueUtil extends BaseContractUtil<GetGovernParamValueTemplate> {


    public GetGovernParamValueUtil(boolean isOnline, String address, String config, String param, Class<GetGovernParamValueTemplate> clazz) throws IOException, CipherException {
        super(isOnline, address, config, param, clazz);
    }

    @Override
    public Function createFunction() throws IOException {
        return ProposalContractX.createGetGovernParamValueFunction(t.getModule(), t.getName());

    }

    @Override
    public String getPposContractAddress() {
        return NetworkParametersUtil.getPposContractAddressOfProposal(nodeConfigModel.getHrp());
    }
}
