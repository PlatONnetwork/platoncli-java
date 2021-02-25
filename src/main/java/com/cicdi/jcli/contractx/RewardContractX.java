package com.cicdi.jcli.contractx;

import com.alaya.abi.solidity.datatypes.BytesType;
import com.alaya.bech32.Bech32;
import com.alaya.contracts.ppos.BaseContract;
import com.alaya.contracts.ppos.abi.CustomStaticArray;
import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.abi.custom.NodeId;
import com.alaya.contracts.ppos.dto.CallResponse;
import com.alaya.contracts.ppos.dto.TransactionResponse;
import com.alaya.contracts.ppos.dto.common.FunctionType;
import com.alaya.contracts.ppos.dto.resp.Reward;
import com.alaya.contracts.ppos.utils.EncoderUtils;
import com.alaya.crypto.Credentials;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.RemoteCall;
import com.alaya.tx.gas.GasProvider;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.util.NetworkParametersUtil;
import com.cicdi.jcli.util.SendUtil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haypo
 * @date 2021/1/8
 */
public class RewardContractX extends BaseContract {
    public RewardContractX(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    public RewardContractX(String contractAddress, long chainId, Web3j web3j, Credentials credentials) {
        super(contractAddress, chainId, web3j, credentials);
    }

    /**
     * 加载合约, 默认ReadonlyTransactionManager事务管理
     *
     * @param web3j web3j对象
     * @return RewardContractX
     */
    public static RewardContractX load(Web3j web3j, String hrp) {
        return new RewardContractX(NetworkParametersUtil.getPposContractAddressOfReward(hrp), web3j);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j       web3j对象
     * @param credentials 钱包
     * @param chainId     链id
     * @return RewardContractX
     */
    public static RewardContractX load(Web3j web3j, Credentials credentials, long chainId, String hrp) {
        return new RewardContractX(NetworkParametersUtil.getPposContractAddressOfReward(hrp), chainId, web3j, credentials);
    }

    public static Function createWithdrawDelegateRewardFunction() {
        return new Function(FunctionType.WITHDRAW_DELEGATE_REWARD_FUNC_TYPE);
    }

    public static Function createGetDelegateRewardFunction(String address, List<String> nodeList) {
        List<NodeId> bytesTypeList = nodeList.stream().map(NodeId::new).collect(Collectors.toList());
        CustomStaticArray<NodeId> dynamicArray = new CustomStaticArray<>(bytesTypeList);
        return new Function(FunctionType.GET_DELEGATE_REWARD_FUNC_TYPE,
                Arrays.asList(new BytesType(Bech32.addressDecode(address)), dynamicArray));
    }

    /**
     * 快速提取账户当前所有的可提取的委托奖励
     */
    public void fastWithdrawDelegateReward(String url, Credentials credentials,
                                           long chainId, BigInteger gasLimit, BigInteger gasPrice, BigInteger nonce) {
        String data = EncoderUtils.functionEncoder(createWithdrawDelegateRewardFunction());
        SendUtil.fastSend(
                new FastHttpService(url),
                BigInteger.ZERO,
                credentials,
                nonce,
                getContractAddress(),
                data,
                chainId,
                gasLimit,
                gasPrice
        );
        SendUtil.waitForFastSend();
    }

    /**
     * 提取账户当前所有的可提取的委托奖励
     *
     * @param gasProvider 用户指定的gasProvider
     * @return RemoteCall<TransactionResponse>
     */
    public RemoteCall<TransactionResponse> withdrawDelegateReward(GasProvider gasProvider) {
        Function function = createWithdrawDelegateRewardFunction();
        return executeRemoteCallTransaction(function, gasProvider);
    }

    /**
     * 查询当前账户地址所委托的节点的NodeID和质押Id
     *
     * @param address  查询的地址
     * @param nodeList 节点id列表
     * @return 当前账户地址所委托的节点的NodeID和质押Id
     */
    public RemoteCall<CallResponse<List<Reward>>> getDelegateReward(String address, List<String> nodeList) {
        Function function = createGetDelegateRewardFunction(address, nodeList);
        return executeRemoteCallListValueReturn(function, Reward.class);
    }
}

