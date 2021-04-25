package com.cicdi.jcli.util.contract.hedge;

import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.contractx.RestrictPlanContractX;
import com.cicdi.jcli.model.Tuple;
import com.cicdi.jcli.template.hedge.CreateRestrictingPlanTemplate;
import com.cicdi.jcli.util.*;
import com.cicdi.jcli.util.contract.BaseContractUtil;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.CallResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author haypo
 * @date 2021/3/15
 */
@SuppressWarnings("unused")
@Slf4j
public class CreateRestrictingPlanUtil extends BaseContractUtil<CreateRestrictingPlanTemplate> {
    public CreateRestrictingPlanUtil(boolean isOnline, String address, String config, String param, Class<CreateRestrictingPlanTemplate> clazz) throws Exception {
        super(isOnline, address, config, param, clazz);
        BigDecimal restrictingMinimumRelease = ConvertUtil.von2Hrp(getRestrictingMinimumRelease());
        for (CreateRestrictingPlanTemplate.Plan plan : t.getPlans()) {
            if (plan.getAmount().compareTo(restrictingMinimumRelease) < 0) {
                System.out.printf(ResourceBundleUtil.getTextString("lowRestrictingAmount"), restrictingMinimumRelease, nodeConfigModel.getHrp());
                if (!StringUtil.readYesOrNo()) {
                    System.out.println(Common.CANCEL_STR);
                    System.exit(0);
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public Tuple<Boolean, String> verifyParam() {
        try {
            BigDecimal restrictingMinimumRelease = ConvertUtil.von2Hrp(getRestrictingMinimumRelease());
            for (CreateRestrictingPlanTemplate.Plan plan : t.getPlans()) {
                if (plan.getAmount().compareTo(restrictingMinimumRelease) < 0) {
                    return Tuple.create(false,
                            String.format(ResourceBundleUtil.getTextString("lowRestrictingAmount") + "\n", restrictingMinimumRelease, nodeConfigModel.getHrp())
                    );
                }
            }
            return passVerifyParam;
        } catch (Exception e) {
            return passVerifyParam;
        }
    }

    private BigInteger getRestrictingMinimumRelease() throws Exception {
        ProposalContractX proposalContractX = ProposalContractX.load(web3j, nodeConfigModel.getHrp());
        CallResponse<String> callResponse = proposalContractX.getGovernParamValue("restricting", "minimumRelease").send();
        return new BigInteger(callResponse.getData());
    }

    @Override
    public String getTemplateSchemaPath() {
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
