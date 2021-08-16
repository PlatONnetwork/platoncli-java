package com.cicdi.jcli.submodule.delegate;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.RewardContractX;
import com.cicdi.jcli.converter.BigIntegerConverter;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.template.BaseTemplate4Serialize;
import com.cicdi.jcli.util.*;
import com.cicdi.jcli.validator.AddressValidator;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.math.BigInteger;
import java.util.Collections;

/**
 * 提取委托奖励
 *
 * @author haypo
 * @date 2021/1/8
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "delegate_withdrawDelegateReward", resourceBundle = "command", commandDescriptionKey = "delegate.withdrawDelegateReward")
public class WithdrawDelegateRewardSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--offline", "-o"}, descriptionKey = "offline")
    protected boolean offline;
    @Parameter(names = {"--gasLimit", "-gasLimit"}, descriptionKey = "gasLimit", converter = BigIntegerConverter.class)
    protected BigInteger gasLimit = Common.MID_GAS_LIMIT;
    @Parameter(names = {"--gasPrice", "-gasPrice"}, descriptionKey = "gasPrice", converter = BigIntegerConverter.class)
    protected BigInteger gasPrice = Common.MID_GAS_PRICE;
    @Parameter(names = {"--address", "-address", "-d"}, descriptionKey = "address", required = true, validateWith = AddressValidator.class)
    protected String address;
    @Parameter(names = {"--fast", "-fast", "-f"}, descriptionKey = "fast")
    protected boolean fast;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        GasPriceUtil.verifyGasPrice(gasPrice);

        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = Web3j.build(new FastHttpService(nodeConfigModel.getRpcAddress()));
        String hrpAddress = AddressUtil.readAddress(address, nodeConfigModel.getHrp());

        //若待领取收益为0则提醒
        if (WalletUtil.getDelegateReward(web3j, nodeConfigModel.getHrp(), hrpAddress).compareTo(BigInteger.ZERO) == 0) {
            StringUtil.warn("Un-withdrew delegate reward is zero, continue? Y/N");
            if (!StringUtil.readYesOrNo()) {
                return Common.CANCEL_STR;
            }
        }

        if (!offline && new File(address).isFile()) {
            File addressFile = AddressUtil.getFileFromAddress(nodeConfigModel.getHrp(), address);
            Credentials credentials = WalletUtil.loadCredentials(StringUtil.readPassword(), addressFile, nodeConfigModel.getHrp());
            RewardContractX rc = RewardContractX.load(web3j, credentials, nodeConfigModel.getHrp());
            if (fast) {
                //快速发送交易
                rc.fastWithdrawDelegateReward(
                        nodeConfigModel.getRpcAddress(),
                        credentials,
                        nodeConfigModel.getChainId(), gasLimit, gasPrice,
                        NonceUtil.getNonce(web3j, hrpAddress, nodeConfigModel.getHrp())
                );
                return Common.SUCCESS_STR;
            } else {
                TransactionReceipt transactionReceipt = rc.withdrawDelegateReward(ConvertUtil.createGasProvider(gasLimit, gasPrice)).send().getTransactionReceipt();
                return TransactionReceiptUtil.handleTxReceipt(transactionReceipt);
            }
        } else {
            BaseTemplate4Serialize baseTemplate4Serialize = new BaseTemplate4Serialize(
                    hrpAddress,
                    Collections.singletonList(NetworkParametersUtil.getPposContractAddressOfReward(nodeConfigModel.getHrp())),
                    EncoderUtils.functionEncoder(RewardContractX.createWithdrawDelegateRewardFunction()),
                    NonceUtil.getNonce(web3j, hrpAddress, nodeConfigModel.getHrp()),
                    BigInteger.ZERO,
                    nodeConfigModel.getChainId(),
                    gasLimit,
                    gasPrice,
                    fast
            );
            return QrUtil.save2QrCodeImage(getQrCodeImagePrefix(), baseTemplate4Serialize);
        }
    }
}
