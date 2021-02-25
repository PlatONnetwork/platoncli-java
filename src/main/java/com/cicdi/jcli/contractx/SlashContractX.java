package com.cicdi.jcli.contractx;

import com.alaya.abi.solidity.datatypes.BytesType;
import com.alaya.abi.solidity.datatypes.Utf8String;
import com.alaya.abi.solidity.datatypes.generated.Uint32;
import com.alaya.abi.solidity.datatypes.generated.Uint64;
import com.alaya.contracts.ppos.BaseContract;
import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.dto.CallResponse;
import com.alaya.contracts.ppos.dto.common.DuplicateSignType;
import com.alaya.contracts.ppos.dto.common.FunctionType;
import com.alaya.crypto.Credentials;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.RemoteCall;
import com.alaya.tx.TransactionManager;
import com.alaya.utils.Numeric;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.util.NetworkParametersUtil;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * 举报合约
 *
 * @author haypo
 * @date 2021/1/11
 */
public class SlashContractX extends BaseContract {

    private SlashContractX(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private SlashContractX(String contractAddress, long chainId, Web3j web3j, Credentials credentials) {
        super(contractAddress, chainId, web3j, credentials);
    }

    private SlashContractX(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(contractAddress, web3j, transactionManager);
    }

    /**
     * 加载合约, 默认ReadonlyTransactionManager事务管理
     *
     * @param web3j web3j对象
     * @return SlashContractX
     */
    public static SlashContractX load(Web3j web3j, String hrp) {
        return new SlashContractX(NetworkParametersUtil.getPposContractAddressOfSlash(hrp), web3j);
    }

    /**
     * 加载合约
     *
     * @param web3j              web3j对象
     * @param transactionManager 交易管理器
     * @param hrp                hrp值
     * @return SlashContractX
     */
    public static SlashContractX load(Web3j web3j, TransactionManager transactionManager, String hrp) {
        return new SlashContractX(NetworkParametersUtil.getPposContractAddressOfSlash(hrp), web3j, transactionManager);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j           web3j对象
     * @param credentials     钱包
     * @param nodeConfigModel 节点配置
     * @return SlashContractX
     */
    public static SlashContractX load(Web3j web3j, Credentials credentials, NodeConfigModel nodeConfigModel) {
        return new SlashContractX(NetworkParametersUtil.getPposContractAddressOfSlash(nodeConfigModel.getHrp()), nodeConfigModel.getChainId(), web3j, credentials);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j       web3j对象
     * @param credentials 钱包
     * @param chainId     链id
     * @return SlashContractX
     */
    public static SlashContractX load(Web3j web3j, Credentials credentials, long chainId, String hrp) {
        return new SlashContractX(NetworkParametersUtil.getPposContractAddressOfSlash(hrp), chainId, web3j, credentials);
    }

    public static Function createReportDoubleSignFunction(DuplicateSignType duplicateSignType, String data) {
        return new Function(FunctionType.REPORT_DOUBLESIGN_FUNC_TYPE,
                Arrays.asList(new Uint32(BigInteger.valueOf(duplicateSignType.getValue())), new Utf8String(data)));
    }

    /**
     * 查询节点是否已被举报过多签
     *
     * @param doubleSignType 代表双签类型，1：prepare，2：viewChange
     * @param nodeId         举报的节点Id
     * @param blockNumber    多签的块高
     * @return 查询结果
     */
    public RemoteCall<CallResponse<String>> checkDoubleSign(DuplicateSignType doubleSignType, String nodeId, BigInteger blockNumber) {
        Function function = new Function(FunctionType.CHECK_DOUBLESIGN_FUNC_TYPE,
                Arrays.asList(new Uint32(doubleSignType.getValue())
                        , new BytesType(Numeric.hexStringToByteArray(nodeId))
                        , new Uint64(blockNumber)));
        return executeRemoteCallObjectValueReturn(function, String.class);
    }

}
