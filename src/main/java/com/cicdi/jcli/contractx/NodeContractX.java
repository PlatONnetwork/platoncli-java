package com.cicdi.jcli.contractx;

import com.alaya.contracts.ppos.BaseContract;
import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.dto.CallResponse;
import com.alaya.contracts.ppos.dto.common.FunctionType;
import com.alaya.contracts.ppos.dto.resp.Node;
import com.alaya.crypto.Credentials;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.RemoteCall;
import com.cicdi.jcli.util.NetworkParametersUtil;

import java.util.List;

/**
 * @author haypo
 * @date 2021/1/8
 */
public class NodeContractX extends BaseContract {

    private NodeContractX(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private NodeContractX(String contractAddress, long chainId, Web3j web3j, Credentials credentials) {
        super(contractAddress, chainId, web3j, credentials);
    }

    /**
     * 加载合约, 默认ReadonlyTransactionManager事务管理
     *
     * @param web3j web3j对象
     * @return NodeContractX
     */
    public static NodeContractX load(Web3j web3j, String hrp) {
        return new NodeContractX(NetworkParametersUtil.getPposContractAddressOfStaking(hrp), web3j);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j       web3j对象
     * @param credentials 钱包
     * @param chainId     链id
     * @return NodeContractX
     */
    public static NodeContractX load(Web3j web3j, Credentials credentials, long chainId, String hrp) {
        return new NodeContractX(NetworkParametersUtil.getPposContractAddressOfStaking(hrp), chainId, web3j, credentials);
    }

    /**
     * @return 当前结算周期的验证人队列
     */
    public RemoteCall<CallResponse<List<Node>>> getVerifierList() {
        Function function = new Function(FunctionType.GET_VERIFIERLIST_FUNC_TYPE);
        return executeRemoteCallListValueReturn(function, Node.class);
    }

    /**
     * @return 当前共识周期的验证人列表
     */
    public RemoteCall<CallResponse<List<Node>>> getValidatorList() {
        Function function = new Function(FunctionType.GET_VALIDATORLIST_FUNC_TYPE);
        return executeRemoteCallListValueReturn(function, Node.class);
    }

    /**
     * @return 所有实时的候选人列表
     */
    public RemoteCall<CallResponse<List<Node>>> getCandidateList() {
        Function function = new Function(FunctionType.GET_CANDIDATELIST_FUNC_TYPE);
        return executeRemoteCallListValueReturn(function, Node.class);
    }
}

