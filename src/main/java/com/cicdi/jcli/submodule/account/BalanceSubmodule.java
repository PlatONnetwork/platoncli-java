package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.RestrictPlanContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.*;
import com.cicdi.jcli.validator.AddressValidator;
import com.platon.contracts.ppos.dto.resp.RestrictingItem;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;

import java.math.BigInteger;

/**
 * 钱包余额
 *
 * @author haypo
 * @date 2021/3/15
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "account_getBalance", resourceBundle = "command", commandDescriptionKey = "account.getBalance")
public class BalanceSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--address", "-address", "-d"}, descriptionKey = "account.getBalance.address", required = true,
            validateWith = AddressValidator.class)
    protected String address;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        String addressFile = AddressUtil.readAddress(address, nodeConfigModel.getHrp());
        Web3j web3j = createWeb3j();
        BigInteger vonBalance = web3j.platonGetBalance(addressFile, DefaultBlockParameterName.LATEST).send().getBalance();
        StringUtil.info("%s: %s %s", ResourceBundleUtil.getTextString("walletBalance"),
                ConvertUtil.von2Hrp(vonBalance).toPlainString(), nodeConfigModel.getHrp());

        RestrictingItem restrictingItem = RestrictPlanContractX.load(web3j, nodeConfigModel.getHrp()).getRestrictingInfo(addressFile).send().getData();
        if (restrictingItem != null) {
            BigInteger vonRestrictBalance = restrictingItem.getBalance();
            StringUtil.info("%s: %s %s", ResourceBundleUtil.getTextString("restrictingBalance"),
                    ConvertUtil.von2Hrp(vonRestrictBalance).toPlainString(), nodeConfigModel.getHrp());
        } else {
            StringUtil.info("%s: 0 %s", ResourceBundleUtil.getTextString("restrictingBalance"), nodeConfigModel.getHrp());
        }

        return Common.SUCCESS_STR;
    }
}
