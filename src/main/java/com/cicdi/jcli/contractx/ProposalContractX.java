package com.cicdi.jcli.contractx;

import com.alaya.abi.solidity.datatypes.BytesType;
import com.alaya.abi.solidity.datatypes.Utf8String;
import com.alaya.abi.solidity.datatypes.generated.Uint32;
import com.alaya.abi.solidity.datatypes.generated.Uint8;
import com.alaya.contracts.ppos.BaseContract;
import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.dto.CallResponse;
import com.alaya.contracts.ppos.dto.TransactionResponse;
import com.alaya.contracts.ppos.dto.common.FunctionType;
import com.alaya.contracts.ppos.dto.enums.VoteOption;
import com.alaya.contracts.ppos.dto.resp.GovernParam;
import com.alaya.contracts.ppos.dto.resp.Proposal;
import com.alaya.contracts.ppos.dto.resp.TallyResult;
import com.alaya.crypto.Credentials;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.RemoteCall;
import com.alaya.protocol.core.methods.response.bean.ProgramVersion;
import com.alaya.tx.TransactionManager;
import com.alaya.tx.gas.GasProvider;
import com.alaya.utils.Numeric;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.util.NetworkParametersUtil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author haypo
 * @date 2021/1/11
 */
public class ProposalContractX extends BaseContract {

    private ProposalContractX(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private ProposalContractX(String contractAddress, long chainId, Web3j web3j, Credentials credentials) {
        super(contractAddress, chainId, web3j, credentials);
    }

    private ProposalContractX(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(contractAddress, web3j, transactionManager);
    }

    /**
     * 加载合约, 默认ReadonlyTransactionManager事务管理
     *
     * @param web3j web3j对象
     * @return ProposalContractX
     */
    public static ProposalContractX load(Web3j web3j, String hrp) {
        return new ProposalContractX(NetworkParametersUtil.getPposContractAddressOfProposal(hrp), web3j);
    }

    /**
     * 加载合约
     *
     * @param web3j              web3j对象
     * @param transactionManager 交易管理器
     * @param hrp                hrp值
     * @return ProposalContractX
     */
    public static ProposalContractX load(Web3j web3j, TransactionManager transactionManager, String hrp) {
        return new ProposalContractX(NetworkParametersUtil.getPposContractAddressOfProposal(hrp), web3j, transactionManager);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j       web3j对象
     * @param credentials 钱包
     * @param chainId     链id
     * @param hrp         hrp值
     * @return ProposalContractX
     */
    public static ProposalContractX load(Web3j web3j, Credentials credentials, long chainId, String hrp) {
        return new ProposalContractX(NetworkParametersUtil.getPposContractAddressOfProposal(hrp), chainId, web3j, credentials);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j           web3j对象
     * @param credentials     钱包
     * @param nodeConfigModel 节点配置
     * @return ProposalContractX
     */
    public static ProposalContractX load(Web3j web3j, Credentials credentials, NodeConfigModel nodeConfigModel) {
        return new ProposalContractX(NetworkParametersUtil.getPposContractAddressOfProposal(nodeConfigModel.getHrp()), nodeConfigModel.getChainId(), web3j, credentials);
    }

    public static Function createDeclareVersionFunction(ProgramVersion programVersion, String verifier) {
        return new Function(FunctionType.DECLARE_VERSION_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(verifier)),
                        new Uint32(programVersion.getProgramVersion()),
                        new BytesType(Numeric.hexStringToByteArray(programVersion.getProgramVersionSign()))));
    }

    public static Function createSubmitProposalFunction(Proposal proposal) {
        Objects.requireNonNull(proposal, "proposal must not be null");
        return new Function(proposal.getSubmitFunctionType(),
                proposal.getSubmitInputParameters());
    }

    public static Function createVoteFunction(ProgramVersion programVersion, String proposalId, String verifier, VoteOption voteOption) {
        return new Function(FunctionType.VOTE_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(verifier)),
                        new BytesType(Numeric.hexStringToByteArray(proposalId)), new Uint8(voteOption.getValue()),
                        new Uint32(programVersion.getProgramVersion()),
                        new BytesType(Numeric.hexStringToByteArray(programVersion.getProgramVersionSign()))));
    }

    public static Function createGetGovernParamValueFunction(String module, String name) {
        return new Function(FunctionType.GET_GOVERN_PARAM_VALUE, Arrays.asList(new Utf8String(module), new Utf8String(name)));
    }

    /**
     * 查询提案
     *
     * @param proposalId 提案id
     * @return 查询结果
     */
    public RemoteCall<CallResponse<Proposal>> getProposal(String proposalId) {
        Function function = new Function(FunctionType.GET_PROPOSAL_FUNC_TYPE,
                Collections.singletonList(new BytesType(Numeric.hexStringToByteArray(proposalId))));
        return executeRemoteCallObjectValueReturn(function, Proposal.class);
    }

    /**
     * 查询提案结果
     *
     * @param proposalId 提案id
     * @return RemoteCall<CallResponse < TallyResult>>
     */
    public RemoteCall<CallResponse<TallyResult>> getTallyResult(String proposalId) {
        Function function = new Function(FunctionType.GET_TALLY_RESULT_FUNC_TYPE,
                Collections.singletonList(new BytesType(Numeric.hexStringToByteArray(proposalId))));
        return executeRemoteCallObjectValueReturn(function, TallyResult.class);
    }

    /**
     * 获取提案列表
     *
     * @return 提案列表
     */
    public RemoteCall<CallResponse<List<Proposal>>> getProposalList() {
        Function function = new Function(FunctionType.GET_PROPOSAL_LIST_FUNC_TYPE,
                Collections.emptyList());
        return executeRemoteCallListValueReturn(function, Proposal.class);
    }

    /**
     * 提交提案
     *
     * @param proposal 包括文本提案和版本提案
     * @return 提价结果
     */
    public RemoteCall<TransactionResponse> submitProposal(Proposal proposal) {
        Function function = createSubmitProposalFunction(proposal);
        return executeRemoteCallTransaction(function);
    }
    public RemoteCall<TransactionResponse> submitProposal(Proposal proposal, GasProvider gasProvider) {
        Function function = createSubmitProposalFunction(proposal);
        return executeRemoteCallTransaction(function,gasProvider);
    }
    /**
     * 查询已生效的版本
     *
     * @return 已生效的版本
     */
    public RemoteCall<CallResponse<BigInteger>> getActiveVersion() {
        Function function = new Function(FunctionType.GET_ACTIVE_VERSION);
        return executeRemoteCallObjectValueReturn(function, BigInteger.class);
    }

    /**
     * 查询当前块高的治理参数值
     *
     * @param module 参数模块
     * @param name   参数名称
     * @return 当前块高的治理参数值
     */
    public RemoteCall<CallResponse<String>> getGovernParamValue(String module, String name) {
        Function function = createGetGovernParamValueFunction(module, name);
        return executeRemoteCallObjectValueReturn(function, String.class);
    }

    /**
     * 查询提案的累积可投票人数
     *
     * @param proposalId 提案ID
     * @param blockHash  块hash
     * @return 提案的累积可投票人数
     */
    public RemoteCall<CallResponse<List<BigInteger>>> getAccuVerifiersCount(String proposalId, String blockHash) {
        Function function = new Function(FunctionType.GET_ACCUVERIFIERS_COUNT, Arrays.asList(new BytesType(Numeric.hexStringToByteArray(proposalId)), new BytesType(Numeric.hexStringToByteArray(blockHash))));
        return executeRemoteCallListValueReturn(function, BigInteger.class);
    }

    /**
     * 查询可治理参数列表
     *
     * @return 可治理参数列表
     */
    public RemoteCall<CallResponse<List<GovernParam>>> getParamList(String module) {
        Function function = new Function(FunctionType.GET_PARAM_LIST, Collections.singletonList(new Utf8String(module)));
        return executeRemoteCallListValueReturn(function, GovernParam.class);
    }
}
