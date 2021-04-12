package com.cicdi.jcli.util.contract.hedge;

import com.platon.contracts.ppos.abi.Function;
import com.platon.crypto.CipherException;
import com.cicdi.jcli.contractx.RestrictPlanContractX;
import com.cicdi.jcli.template.hedge.CreateRestrictingPlanTemplate;
import com.cicdi.jcli.util.NetworkParametersUtil;
import com.cicdi.jcli.util.contract.BaseContractUtil;

import java.io.IOException;

/**
 * @author haypo
 * @date 2021/3/15
 */
@SuppressWarnings("unused")
public class CreateRestrictingPlanUtil extends BaseContractUtil<CreateRestrictingPlanTemplate> {
    public CreateRestrictingPlanUtil(boolean isOnline, String address, String config, String param, Class<CreateRestrictingPlanTemplate> clazz) throws IOException, CipherException {
        super(isOnline, address, config, param, clazz);
    }

    @Override
    protected String getTemplateSchemaPath() {
        return "/json/CreateRestrictingPlanTemplateSchema.json";
    }

    @Override
    public Function createFunction() throws IOException {
        return RestrictPlanContractX.createRestrictingPlanFunctionX(t.getAccount(), t.getPlans());
    }

    @Override
    public String getPposContractAddress() {
        return NetworkParametersUtil.getPposContractAddressOfRestrictingPlan(nodeConfigModel.getHrp());
    }
}
