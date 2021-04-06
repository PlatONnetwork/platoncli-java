package com.cicdi.jcli.util.contract.delegate;

import com.cicdi.jcli.contractx.DelegateContractX;
import com.cicdi.jcli.template.delegate.DelegateNewTemplate;
import com.cicdi.jcli.util.*;
import com.cicdi.jcli.util.contract.BaseContractUtil;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.enums.StakingAmountType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author haypo
 * @date 2021/1/4
 */
@Slf4j
public class DelegateNewUtil extends BaseContractUtil<DelegateNewTemplate> {
    public DelegateNewUtil(boolean isOnline, String address, String config, String param, Class<DelegateNewTemplate> clazz) throws Exception {
        super(isOnline, address, config, param, clazz);
        //委托大于余额
        BigDecimal balance = ConvertUtil.von2Hrp(WalletUtil.getBalance(web3j, AddressUtil.readAddress(address, nodeConfigModel.getHrp())));
        if (t.getAmount().compareTo(balance) > 0) {
            System.out.println("Amount: " + t.getAmount() + " is greater than balance: " + balance + nodeConfigModel.getHrp() + ", so delegation has no sense, continue? Y/N");
            if (!StringUtil.readYesOrNo()) {
                log.info(Common.CANCEL_STR);
                System.exit(0);
            }
        }

        //委托小于阈值
        BigDecimal threshold = ConvertUtil.von2Hrp(Web3jUtil.getStakingOperatingThreshold(web3j, nodeConfigModel.getHrp()));
        if (t.getAmount().compareTo(threshold) < 0) {
            System.out.println("Amount: " + t.getAmount() + " is less than threshold:" + threshold + ", so delegation has no sense, continue? Y/N");
            if (!StringUtil.readYesOrNo()) {
                log.info(Common.CANCEL_STR);
                System.exit(0);
            }
        }

        VerifyUtil.verifyNodeId(web3j, nodeConfigModel.getHrp(), t.getNodeId());
    }

    @Override
    public String getTemplateSchemaPath() {
        return "/json/DelegateNewTemplateSchema.json";
    }

    @Override
    public Function createFunction() throws IOException {
        StakingAmountType sta = t.getType();
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
