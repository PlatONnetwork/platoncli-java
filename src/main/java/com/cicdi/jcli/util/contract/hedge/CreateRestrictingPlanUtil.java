package com.cicdi.jcli.util.contract.hedge;

import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.contractx.RestrictPlanContractX;
import com.cicdi.jcli.model.Tuple;
import com.cicdi.jcli.template.hedge.CreateRestrictingPlanTemplate;
import com.cicdi.jcli.util.ConvertUtil;
import com.cicdi.jcli.util.NetworkParametersUtil;
import com.cicdi.jcli.util.ResourceBundleUtil;
import com.cicdi.jcli.util.WalletUtil;
import com.cicdi.jcli.util.contract.BaseContractUtil;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.CallResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

/**
 * @author haypo
 * @date 2021/3/15
 */
@SuppressWarnings("unused")
@Slf4j
public class CreateRestrictingPlanUtil extends BaseContractUtil<CreateRestrictingPlanTemplate> {
    public CreateRestrictingPlanUtil(boolean isOnline, String address, String config, String param, Class<CreateRestrictingPlanTemplate> clazz) throws Exception {
        super(isOnline, address, config, param, clazz);
    }

    @Override
    public Tuple<Boolean, String> verifyParam() {
        try {
            //验证锁仓金额是否达到最低限额
            BigDecimal restrictingMinimumRelease = ConvertUtil.von2Hrp(getRestrictingMinimumRelease());
            for (CreateRestrictingPlanTemplate.Plan plan : t.getPlans()) {
                if (plan.getAmount().compareTo(restrictingMinimumRelease) < 0) {
                    return Tuple.create(false,
                            String.format(ResourceBundleUtil.getTextString("lowRestrictingAmount") + "\n", restrictingMinimumRelease, nodeConfigModel.getHrp())
                    );
                }
            }
            Iterator<BigDecimal> amountIterator = t.getPlans().stream().map(CreateRestrictingPlanTemplate.Plan::getAmount).iterator();
            BigDecimal result = BigDecimal.ZERO;
            while (amountIterator.hasNext()) {
                result = result.add(amountIterator.next());
            }
            BigDecimal hrpBalance = ConvertUtil.von2Hrp(WalletUtil.getBalance(web3j, address));
            if (result.compareTo(hrpBalance) > 0) {
                return Tuple.create(false,
                        String.format(ResourceBundleUtil.getTextString("lowBalance") + "\n", hrpBalance, nodeConfigModel.getHrp())
                );
            }
            return passVerifyParam;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
