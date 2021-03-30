package com.cicdi.jcli.contractx;

import com.platon.abi.solidity.datatypes.BytesType;
import com.platon.abi.solidity.datatypes.generated.Uint16;
import com.platon.abi.solidity.datatypes.generated.Uint256;
import com.platon.contracts.ppos.BaseContract;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.TransactionResponse;
import com.platon.contracts.ppos.dto.common.FunctionType;
import com.platon.contracts.ppos.dto.enums.StakingAmountType;
import com.platon.contracts.ppos.dto.req.StakingParam;
import com.platon.contracts.ppos.dto.req.UpdateStakingParam;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.RemoteCall;
import com.platon.tx.TransactionManager;
import com.platon.tx.gas.GasProvider;
import com.platon.utils.Numeric;
import com.cicdi.jcli.util.NetworkParametersUtil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * 质押合约
 *
 * @author haypo
 * @date 2021/1/8
 */
public class StakingContractX extends BaseContract {

    private StakingContractX(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private StakingContractX(String contractAddress, long chainId, Web3j web3j, Credentials credentials) {
        super(contractAddress, web3j, credentials);
    }

    private StakingContractX(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(contractAddress, web3j, transactionManager);
    }

    /**
     * 加载合约, 默认ReadonlyTransactionManager事务管理
     *
     * @param web3j web3j对象
     * @return StakingContractX
     */
    public static StakingContractX load(Web3j web3j, String hrp) {
        return new StakingContractX(NetworkParametersUtil.getPposContractAddressOfStaking(hrp), web3j);
    }

    /**
     * 加载合约
     *
     * @param web3j              web3j对象
     * @param transactionManager 交易管理器
     * @return StakingContractX
     */
    public static StakingContractX load(Web3j web3j, TransactionManager transactionManager, String hrp) {
        return new StakingContractX(NetworkParametersUtil.getPposContractAddressOfStaking(hrp), web3j, transactionManager);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j       web3j对象
     * @param credentials 钱包
     * @param chainId     链id
     * @return StakingContractX
     */
    public static StakingContractX load(Web3j web3j, Credentials credentials, long chainId, String hrp) {
        return new StakingContractX(NetworkParametersUtil.getPposContractAddressOfStaking(hrp), chainId, web3j, credentials);
    }

    public static Function createStakingFunction(StakingParam stakingParam) {
        return new Function(
                FunctionType.STAKING_FUNC_TYPE,
                stakingParam.getSubmitInputParameters());
    }

    public static Function createUnStakingFunction(String nodeId) {
        return new Function(FunctionType.WITHDREW_STAKING_FUNC_TYPE,
                Collections.singletonList(new BytesType(Numeric.hexStringToByteArray(nodeId))));
    }

    public static Function createUpdateStakingFunction(UpdateStakingParam updateStakingParam) {
        return new Function(FunctionType.UPDATE_STAKING_INFO_FUNC_TYPE,
                updateStakingParam.getSubmitInputParameters());
    }

    public static Function createAddStakingFunction(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        return new Function(FunctionType.ADD_STAKING_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(nodeId)),
                        new Uint16(stakingAmountType.getValue()),
                        new Uint256(amount)));
    }

    /**
     * 获取质押信息
     *
     * @param nodeId 节点id
     * @return 交易结果
     */
    public RemoteCall<CallResponse<Node>> getStakingInfo(String nodeId) {
        Function function = new Function(FunctionType.GET_STAKINGINFO_FUNC_TYPE,
                Collections.singletonList(new BytesType(Numeric.hexStringToByteArray(nodeId))));
        return executeRemoteCallObjectValueReturn(function, Node.class);
    }

    /**
     * @return 当前结算周期的区块奖励
     */
    public RemoteCall<CallResponse<BigInteger>> getPackageReward() {
        Function function = new Function(FunctionType.GET_PACKAGEREWARD_FUNC_TYPE);
        return executeRemoteCallObjectValueReturn(function, BigInteger.class);
    }

    /**
     * @return 当前结算周期的质押奖励
     */
    public RemoteCall<CallResponse<BigInteger>> getStakingReward() {
        Function function = new Function(FunctionType.GET_STAKINGREWARD_FUNC_TYPE);
        return executeRemoteCallObjectValueReturn(function, BigInteger.class);
    }

    /**
     * @return 打包区块的平均时间
     */
    public RemoteCall<CallResponse<BigInteger>> getAvgPackTime() {
        Function function = new Function(FunctionType.GET_AVGPACKTIME_FUNC_TYPE);
        return executeRemoteCallObjectValueReturn(function, BigInteger.class);
    }

    /**
     * 发起质押
     *
     * @param stakingParam 质押参数
     * @return 交易结果
     * @see StakingParam
     */
    public RemoteCall<TransactionResponse> staking(StakingParam stakingParam) {
        Function function = createStakingFunction(stakingParam);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 发起质押
     *
     * @param stakingParam 质押参数
     * @param gasProvider  gas提供器
     * @return 交易结果
     * @see StakingParam
     */
    public RemoteCall<TransactionResponse> staking(StakingParam stakingParam, GasProvider gasProvider) {
        Function function = createStakingFunction(stakingParam);
        return executeRemoteCallTransaction(function, gasProvider);
    }
}

