package com.cicdi.jcli.submodule.delegate;

import com.alaya.contracts.ppos.dto.TransactionResponse;
import com.alaya.contracts.ppos.dto.common.ErrorCode;
import com.alaya.contracts.ppos.utils.EncoderUtils;
import com.alaya.crypto.Credentials;
import com.alaya.crypto.WalletUtils;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
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

import java.io.File;
import java.math.BigInteger;
import java.util.Collections;

/**
 * 提取委托奖励
 *
 * @author haypo
 * @date 2021/1/8
 */
@Parameters(commandNames = "delegate_withdrawDelegateReward", commandDescription = "提取委托奖励")
public class WithdrawDelegateRewardSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--offline", "-o"}, description = "在线交易或者离线交易. 不输入默认为在线交易, 并生成二维码图片放置在桌面上，提供ATON离线扫码签名")
    protected boolean offline;
    @Parameter(names = {"--gasLimit", "-gasLimit"}, description = "gas用量限制", converter = BigIntegerConverter.class)
    protected BigInteger gasLimit = Common.MID_GAS_LIMIT;
    @Parameter(names = {"--gasPrice", "-gasPrice"}, description = "gas价格", converter = BigIntegerConverter.class)
    protected BigInteger gasPrice = Common.MID_GAS_PRICE;
    @Parameter(names = {"--address", "-address", "-d"}, description = "发送交易地址或者名称.json", required = true)
    protected String address;
    @Parameter(names = {"--fast", "-fast", "-f"}, description = "是否使用快速发送功能，默认不使用")
    protected boolean fast;

    public boolean isOnline() {
        return !offline && new File(address).isFile();
    }

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = Web3j.build(new FastHttpService(nodeConfigModel.getRpcAddress()));

        if (isOnline()) {
            Credentials credentials = WalletUtils.loadCredentials(StringUtil.readPassword(), address);
            RewardContractX rc = RewardContractX.load(web3j, credentials, nodeConfigModel.getChainId(), nodeConfigModel.getHrp());
            if (fast) {
                rc.fastWithdrawDelegateReward(
                        nodeConfigModel.getRpcAddress(),
                        credentials,
                        nodeConfigModel.getChainId(), gasLimit, gasPrice,
                        NonceUtil.getNonce(web3j, ParamUtil.parseAddress(address, nodeConfigModel.getHrp()), nodeConfigModel.getHrp())
                );
                return Common.SUCCESS_STR;
            } else {
                TransactionReceipt transactionReceipt = rc.withdrawDelegateReward(ConvertUtil.genGasProvider(gasLimit, gasPrice)).send().getTransactionReceipt();
                TransactionResponse response = getResponseFromTransactionReceipt(transactionReceipt);
                return response.isStatusOk() ? Common.SUCCESS_STR : Common.FAIL_STR + ":" + ErrorCode.getErrorMsg(response.getCode());
            }
        } else {
            String parsedAddress = ParamUtil.parseAddress(address, nodeConfigModel.getHrp());
            BaseTemplate4Serialize baseTemplate4Serialize = new BaseTemplate4Serialize(
                    parsedAddress,
                    Collections.singletonList(NetworkParametersUtil.getPposContractAddressOfReward(nodeConfigModel.getHrp())),
                    EncoderUtils.functionEncoder(RewardContractX.createWithdrawDelegateRewardFunction()),
                    NonceUtil.getNonce(web3j, parsedAddress, nodeConfigModel.getHrp()),
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
