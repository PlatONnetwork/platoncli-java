package com.cicdi.jcli.contractx;

import com.alaya.abi.solidity.datatypes.BytesType;
import com.alaya.abi.solidity.datatypes.generated.Uint16;
import com.alaya.abi.solidity.datatypes.generated.Uint256;
import com.alaya.abi.solidity.datatypes.generated.Uint64;
import com.alaya.bech32.Bech32;
import com.alaya.contracts.ppos.BaseContract;
import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.dto.CallResponse;
import com.alaya.contracts.ppos.dto.TransactionResponse;
import com.alaya.contracts.ppos.dto.common.FunctionType;
import com.alaya.contracts.ppos.dto.enums.StakingAmountType;
import com.alaya.contracts.ppos.dto.resp.DelegationIdInfo;
import com.alaya.crypto.Credentials;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.RemoteCall;
import com.alaya.tx.TransactionManager;
import com.alaya.tx.gas.GasProvider;
import com.alaya.utils.Numeric;
import com.cicdi.jcli.template.delegate.DelegateNewTemplate;
import com.cicdi.jcli.util.ConvertUtil;
import com.cicdi.jcli.util.NetworkParametersUtil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author haypo
 * @date 2021/1/8
 */
public class DelegateContractX extends BaseContract {

    private DelegateContractX(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private DelegateContractX(String contractAddress, long chainId, Web3j web3j, Credentials credentials) {
        super(contractAddress, chainId, web3j, credentials);
    }

    private DelegateContractX(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(contractAddress, web3j, transactionManager);
    }

    /**
     * 加载合约, 默认ReadonlyTransactionManager事务管理
     *
     * @param web3j web3j对象
     * @return DelegateContractX
     */
    public static DelegateContractX load(Web3j web3j, String hrp) {
        return new DelegateContractX(NetworkParametersUtil.getPposContractAddressOfStaking(hrp), web3j);
    }

    /**
     * 加载合约
     *
     * @param web3j              web3j对象
     * @param transactionManager 交易管理器
     * @return DelegateContractX
     * @see TransactionManager
     * @see Web3j
     */
    public static DelegateContractX load(Web3j web3j, TransactionManager transactionManager, String hrp) {
        return new DelegateContractX(NetworkParametersUtil.getPposContractAddressOfStaking(hrp), web3j, transactionManager);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j       web3j对象
     * @param credentials 钱包
     * @param chainId     链id
     * @return DelegateContractX
     * @see Credentials
     * @see Web3j
     */
    public static DelegateContractX load(Web3j web3j, Credentials credentials, long chainId, String hrp) {
        return new DelegateContractX(NetworkParametersUtil.getPposContractAddressOfStaking(hrp), chainId, web3j, credentials);
    }

    /**
     * @param nodeId          节点id
     * @param stakingBlockNum 质押块高
     * @param amount          质押数量，单位为von
     * @return 质押方法
     * @see Function
     */
    public static Function createUnDelegateFunction(String nodeId, BigInteger stakingBlockNum, BigInteger amount) {
        return new Function(FunctionType.WITHDREW_DELEGATE_FUNC_TYPE,
                Arrays.asList(new Uint64(stakingBlockNum)
                        , new BytesType(Numeric.hexStringToByteArray(nodeId))
                        , new Uint256(amount)));
    }

    public static Function createDelegateFunction(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        return new Function(FunctionType.DELEGATE_FUNC_TYPE,
                Arrays.asList(new Uint16(stakingAmountType.getValue())
                        , new BytesType(Numeric.hexStringToByteArray(nodeId))
                        , new Uint256(amount)));
    }

    private static StakingAmountType parseStakingAmountType(int type) {
        switch (type) {
            case 0:
                return StakingAmountType.FREE_AMOUNT_TYPE;
            case 1:
                return StakingAmountType.RESTRICTING_AMOUNT_TYPE;
            default:
                throw new RuntimeException("type error!");
        }
    }

    /**
     * 发起委托
     *
     * @param nodeId            被质押的节点的NodeId
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做委托，0: 自由金额； 1: 锁仓金额
     * @param amount            委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @return 发起委托结果
     */
    public RemoteCall<TransactionResponse> delegate(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        Function function = createDelegateFunction(nodeId, stakingAmountType, amount);
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionResponse> delegate(DelegateNewTemplate template, GasProvider gasProvider) {
        return delegate(template.getNodeId(),
                parseStakingAmountType(template.getType()),
                ConvertUtil.hrp2Von(template.getAmount()),
                gasProvider);
    }

    /**
     * 发起委托
     *
     * @param nodeId            被质押的节点的NodeId
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做委托，0: 自由金额； 1: 锁仓金额
     * @param amount            委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @param gasProvider       用户指定的gasProvider
     * @return 委托结果
     */
    public RemoteCall<TransactionResponse> delegate(String nodeId, StakingAmountType stakingAmountType, BigInteger amount, GasProvider gasProvider) {
        Function function = createDelegateFunction(nodeId, stakingAmountType, amount);
        return executeRemoteCallTransaction(function, gasProvider);
    }

    /**
     * @param address 地址
     * @return 查询当前账户地址所委托的节点的NodeID和质押Id
     */
    public RemoteCall<CallResponse<List<DelegationIdInfo>>> getRelatedListByDelAddr(String address) {
        Function function = new Function(FunctionType.GET_DELEGATELIST_BYADDR_FUNC_TYPE,
                Collections.singletonList(new BytesType(Bech32.addressDecode(address))));
        return executeRemoteCallListValueReturn(function, DelegationIdInfo.class);
    }
}
