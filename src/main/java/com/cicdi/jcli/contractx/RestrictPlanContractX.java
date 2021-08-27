package com.cicdi.jcli.contractx;

import com.platon.abi.solidity.datatypes.BytesType;
import com.platon.bech32.Bech32;
import com.platon.contracts.ppos.BaseContract;
import com.platon.contracts.ppos.abi.CustomStaticArray;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.RestrictingPlan;
import com.platon.contracts.ppos.dto.TransactionResponse;
import com.platon.contracts.ppos.dto.common.FunctionType;
import com.platon.contracts.ppos.dto.resp.RestrictingItem;
import com.platon.contracts.ppos.exception.EstimateGasException;
import com.platon.contracts.ppos.exception.NoSupportFunctionType;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.RemoteCall;
import com.platon.protocol.core.methods.response.PlatonSendTransaction;
import com.platon.tx.TransactionManager;
import com.platon.tx.gas.GasProvider;
import com.cicdi.jcli.template.hedge.CreateRestrictingPlanTemplate;
import com.cicdi.jcli.util.NetworkParametersUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author haypo
 * @date 2021/1/11
 * @see com.platon.contracts.ppos.dto.RestrictingPlan
 */
@SuppressWarnings("unused")
public class RestrictPlanContractX extends BaseContract {

    private RestrictPlanContractX(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private RestrictPlanContractX(String contractAddress, long chainId, Web3j web3j, Credentials credentials) {
        super(contractAddress, web3j, credentials);
    }

    private RestrictPlanContractX(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(contractAddress, web3j, transactionManager);
    }

    /**
     * 加载合约, 默认ReadonlyTransactionManager事务管理
     *
     * @param web3j web3j对象
     * @return RestrictPlanContractX
     */
    public static RestrictPlanContractX load(Web3j web3j, String hrp) {
        return new RestrictPlanContractX(NetworkParametersUtil.getPposContractAddressOfRestrictingPlan(hrp), web3j);
    }

    /**
     * 加载合约
     *
     * @param web3j              web3j对象
     * @param transactionManager 交易管理器
     * @return RestrictPlanContractX
     */
    public static RestrictPlanContractX load(Web3j web3j, TransactionManager transactionManager, String hrp) {
        return new RestrictPlanContractX(NetworkParametersUtil.getPposContractAddressOfRestrictingPlan(hrp), web3j, transactionManager);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j       web3j对象
     * @param credentials 钱包
     * @param chainId     链id
     * @return RestrictPlanContractX
     */
    public static RestrictPlanContractX load(Web3j web3j, Credentials credentials, long chainId, String hrp) {
        return new RestrictPlanContractX(NetworkParametersUtil.getPposContractAddressOfRestrictingPlan(hrp), chainId, web3j, credentials);
    }

    /**
     * 创建锁仓计划方法
     *
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @return 锁仓方法
     */
    public static Function createRestrictingPlanFunction(String account, List<RestrictingPlan> restrictingPlanList) {
        return new Function(
                FunctionType.CREATE_RESTRICTINGPLAN_FUNC_TYPE,
                Arrays.asList(new BytesType(Bech32.addressDecode(account)), new CustomStaticArray<>(restrictingPlanList)));
    }

    public static Function createRestrictingPlanFunctionX(String account, List<CreateRestrictingPlanTemplate.Plan> restrictingPlanList) {
        return new Function(
                FunctionType.CREATE_RESTRICTINGPLAN_FUNC_TYPE,
                Arrays.asList(new BytesType(Bech32.addressDecode(account)), new CustomStaticArray<>(restrictingPlanList)));
    }

    public static Function createGetRestrictingInfoFunction(String account) {
        return new Function(
                FunctionType.GET_RESTRICTINGINFO_FUNC_TYPE,
                Collections.singletonList(new BytesType(Bech32.addressDecode(account))));
    }

    /**
     * 创建锁仓计划
     *
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @return 交易结果
     */
    public RemoteCall<TransactionResponse> createRestrictingPlan(String account, List<RestrictingPlan> restrictingPlanList) {
        Function function = createRestrictingPlanFunction(account, restrictingPlanList);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 创建锁仓计划
     *
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @param gasProvider         gas提供器
     * @return 交易结果
     */
    public RemoteCall<TransactionResponse> createRestrictingPlan(String account, List<RestrictingPlan> restrictingPlanList, GasProvider gasProvider) {
        Function function = createRestrictingPlanFunction(account, restrictingPlanList);
        return executeRemoteCallTransaction(function, gasProvider);
    }

    /**
     * 获取创建锁仓计划的gasProvider
     *
     * @param account             账户
     * @param restrictingPlanList 锁仓计划列表
     * @return gas提供器
     */
    public GasProvider getCreateRestrictingPlan(String account, List<RestrictingPlan> restrictingPlanList) throws IOException, EstimateGasException {
        Function function = createRestrictingPlanFunction(account, restrictingPlanList);
        return getDefaultGasProvider(function);
    }

    /**
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @return 交易hash
     */
    public RemoteCall<PlatonSendTransaction> createRestrictingPlanReturnTransaction(String account, List<RestrictingPlan> restrictingPlanList) {
        Function function = createRestrictingPlanFunction(account, restrictingPlanList);
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @param gasProvider         gas提供器
     * @return 交易hash
     */
    public RemoteCall<PlatonSendTransaction> createRestrictingPlanReturnTransaction(String account, List<RestrictingPlan> restrictingPlanList, GasProvider gasProvider) {
        Function function = createRestrictingPlanFunction(account, restrictingPlanList);
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    /**
     * 获取锁仓信息
     *
     * @param account 锁仓释放到账账户
     * @return 锁仓信息
     */
    public RemoteCall<CallResponse<RestrictingItem>> getRestrictingInfo(String account) {
        Function function = createGetRestrictingInfoFunction(account);
        return executeRemoteCallObjectValueReturn(function, RestrictingItem.class);
    }
}
