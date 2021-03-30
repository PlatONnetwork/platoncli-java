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
    public DelegateUnDelegateUtil(boolean isOnline, String address, String config, String param, Class<DelegateUnDelegateTemplate> clazz) throws Exception {
        super(isOnline, address, config, param, clazz);
        //赎回委托小于阈值
        BigInteger threshold = Web3jUtil.getStakingOperatingThreshold(web3j, nodeConfigModel.getHrp());
        if (this.t.getAmount().compareTo(ConvertUtil.von2Hrp(threshold)) < 0 ||
                this.t.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Amount is less than threshold, so un-delegation has no sense, continue? Y/N");
            if (!StringUtil.readYesOrNo()) {
                log.info(Common.CANCEL_STR);
                System.exit(0);
            }
        }

        if (this.t.getAmount().compareTo(ConvertUtil.von2Hrp(WalletUtil.getDelegateTotal(web3j, nodeConfigModel.getHrp(), credentials.getAddress()))) > 0) {
            System.out.println("Amount is greater than delegated, so un-delegation has no sense, continue? Y/N");
            if (!StringUtil.readYesOrNo()) {
                log.info(Common.CANCEL_STR);
                System.exit(0);
            }
        }

        //验证节点id
        VerifyUtil.verifyNodeId(web3j, nodeConfigModel.getHrp(), t.getNodeId());
        //验证节点质押块高
        VerifyUtil.verifyStakingBlockNum(web3j, nodeConfigModel.getHrp(), t.getNodeId(), t.getStakingBlockNum());

        VerifyUtil.verifyDelNodeIdAndStakingBlockNum(web3j, nodeConfigModel.getHrp(), t.getNodeId(), credentials.getAddress(), t.getStakingBlockNum());
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
