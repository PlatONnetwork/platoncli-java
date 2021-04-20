package com.cicdi.jcli.util.contract.delegate;

import com.cicdi.jcli.contractx.DelegateContractX;
import com.cicdi.jcli.template.delegate.DelegateUnDelegateTemplate;
import com.cicdi.jcli.util.*;
import com.cicdi.jcli.util.contract.BaseContractUtil;
import com.platon.contracts.ppos.abi.Function;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author haypo
 * @date 2021/1/8
 */
@Slf4j
public class DelegateUnDelegateUtil extends BaseContractUtil<DelegateUnDelegateTemplate> {
    @Override
    public String getTemplateSchemaPath() {
        return "/json/DelegateUnDelegateTemplateSchema.json";
    }

    public DelegateUnDelegateUtil(boolean isOnline, String address, String config, String param, Class<DelegateUnDelegateTemplate> clazz) throws Exception {
        super(isOnline, address, config, param, clazz);
        //赎回委托小于阈值
        BigInteger thresholdVon = Web3jUtil.getStakingOperatingThreshold(web3j, nodeConfigModel.getHrp());
        BigDecimal thresholdHrp = ConvertUtil.von2Hrp(thresholdVon);
        if (this.t.getAmount().compareTo(thresholdHrp) < 0 ||
                this.t.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Amount: " + t.getAmount() + nodeConfigModel.getHrp() +
                    " is less than threshold: " + thresholdHrp + nodeConfigModel.getHrp() +
                    ", so un-delegation has no sense, continue? Y/N");
            if (!StringUtil.readYesOrNo()) {
                log.info(Common.CANCEL_STR);
                System.exit(0);
            }
        }

        BigInteger delegatedVon = WalletUtil.getDelegateTotal(web3j, nodeConfigModel.getHrp(),
                AddressUtil.readAddress(address, nodeConfigModel.getHrp()));
        BigDecimal delegatedHrp = ConvertUtil.von2Hrp(delegatedVon);
        if (this.t.getAmount().compareTo(delegatedHrp) > 0) {
            System.out.println("Amount: " + t.getAmount() + nodeConfigModel.getHrp() +
                    " is greater than delegated: " + delegatedHrp + nodeConfigModel.getHrp() +
                    ", so un-delegation has no sense, continue? Y/N");
            if (!StringUtil.readYesOrNo()) {
                log.info(Common.CANCEL_STR);
                System.exit(0);
            }
        }

        //验证节点id
        VerifyUtil.verifyNodeId(web3j, nodeConfigModel.getHrp(), t.getNodeId());
        //验证节点质押块高
        VerifyUtil.verifyStakingBlockNum(web3j, nodeConfigModel.getHrp(), t.getNodeId(), t.getStakingBlockNum());

        VerifyUtil.verifyDelNodeIdAndStakingBlockNum(web3j, nodeConfigModel.getHrp(), t.getNodeId(),
                AddressUtil.readAddress(address, nodeConfigModel.getHrp()),
                t.getStakingBlockNum());
    }

    @Override
    public Function createFunction() throws IOException {
        if (StringUtil.isBlank(t.getNodeId())) {
            t.setNodeId(Web3jUtil.getNodeId(nodeConfigModel.getRpcAddress()));
        }
        return DelegateContractX.createUnDelegateFunction(
                t.getNodeId(),
                t.getStakingBlockNum(),
                ConvertUtil.hrp2Von(t.getAmount())
        );
    }

    @Override
    public String getPposContractAddress() {
        return NetworkParametersUtil.getPposContractAddressOfStaking(nodeConfigModel.getHrp());
    }
}
