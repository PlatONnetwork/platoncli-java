package com.cicdi.jcli.submodule.government;

import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.dto.TransactionResponse;
import com.alaya.contracts.ppos.dto.resp.Proposal;
import com.alaya.crypto.Credentials;
import com.alaya.crypto.WalletUtils;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.RemoteCall;
import com.alaya.tx.gas.GasProvider;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.template.BaseTemplate4Serialize;
import com.cicdi.jcli.template.government.CancelProposalTemplate;
import com.cicdi.jcli.template.government.ParamProposalTemplate;
import com.cicdi.jcli.template.government.VersionProposalTemplate;
import com.cicdi.jcli.util.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 提交参数/升级/取消提案
 *
 * @author haypo
 * @date 2021/1/14
 */
@Slf4j
@Parameters(commandNames = "government_submitProposal", commandDescription = "提交参数/升级/取消提案")
public class SubmitProposalSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--offline", "-o"}, description = "在线交易或者离线交易. 不输入默认为在线交易, 并生成二维码图片放置在桌面上，提供ATON离线扫码签名")
    protected boolean offline;
    @Parameter(names = {"--template", "-template", "-t"}, help = true, description = "查看委托交易参数模板，与其他参数共存没有效果，单独执行查看")
    protected boolean template;
    @Parameter(names = {"--param", "-param", "-p"}, description = "交易参数json字符串，或者交易参数json文件路径", required = true)
    protected String param;
    @Parameter(names = {"--address", "-address", "-d"}, description = "发送交易地址或者名称.json", required = true)
    protected String address;
    @Parameter(names = {"--module", "-module", "-m"}, description = "提案类型, 包括: CancelProposal(取消提案), ParamProposal(参数提案), VersionProposal(升级提案)", required = true)
    protected String module;
    @Parameter(names = {"--fast", "-fast", "-f"}, description = "是否使用快速发送功能，默认不使用")
    protected boolean fast;

    public boolean isOnline() {
        return !offline && new File(address).isFile();
    }

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        if (template && argv.length == Common.TWO) {
            return '\n' +
                    "CancelProposalTemplate:\n" +
                    "类型             必填性         参数名称              参数解释\n" +
                    "String          must          verifier            提交提案的验证人，nodeId\n" +
                    "String          must          piPid               PIPID\n" +
                    "BigInteger      must          endVotingRound      投票共识轮数量\n" +
                    "String          must          canceledProposalId  待取消的提案ID\n" +
                    "ParamProposalTemplate:\n" +
                    "类型             必填性         参数名称             参数解释\n" +
                    "String          must          verifier            提交提案的验证人，nodeId\n" +
                    "String          must          piPid               PIPID\n" +
                    "String          must          module              参数模块\n" +
                    "String          must          name                参数名称\n" +
                    "String          must          newValue            参数新值\n" +
                    "VersionProposalTemplate:\n" +
                    "类型             必填性         参数名称             参数解释\n" +
                    "String          must          verifier            提交提案的验证人，nodeId\n" +
                    "String          must          piPid               PIPID\n" +
                    "BigInteger      must          endVotingRound      投票共识轮数量\n" +
                    "BigInteger      must          newVersion          升级版本\n";
        }

        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = Web3j.build(new FastHttpService(nodeConfigModel.getRpcAddress()));
        Proposal proposal;
        switch (module) {
            case "cancel_proposal":
            case "cancelProposal":
            case "CancelProposal":
                CancelProposalTemplate cancelProposalTemplate = ParamUtil.readParam(param, CancelProposalTemplate.class);
                proposal = Proposal.createSubmitCancelProposalParam(
                        cancelProposalTemplate.getVerifier(),
                        cancelProposalTemplate.getPiPid(),
                        cancelProposalTemplate.getEndVotingRound(),
                        cancelProposalTemplate.getCanceledProposalId()
                );
                break;
            case "param_proposal":
            case "paramProposal":
            case "ParamProposal":
                ParamProposalTemplate paramProposalTemplate = ParamUtil.readParam(param, ParamProposalTemplate.class);
                proposal = Proposal.createSubmitParamProposalParam(
                        paramProposalTemplate.getVerifier(),
                        paramProposalTemplate.getPiPid(),
                        paramProposalTemplate.getModule(),
                        paramProposalTemplate.getName(),
                        paramProposalTemplate.getNewValue()
                );
                break;
            case "version_proposal":
            case "versionProposal":
            case "VersionProposal":
                VersionProposalTemplate versionProposalTemplate = ParamUtil.readParam(param, VersionProposalTemplate.class);
                proposal = Proposal.createSubmitVersionProposalParam(
                        versionProposalTemplate.getVerifier(),
                        versionProposalTemplate.getPiPid(),
                        versionProposalTemplate.getEndVotingRound(),
                        versionProposalTemplate.getNewVersion()
                );
                break;
            default:
                throw new RuntimeException("module parameter error!");
        }

        Function function = ProposalContractX.createSubmitProposalFunction(proposal);
        GasProvider gasProvider = Common.getDefaultGasProvider(function);
        if (isOnline()) {
            String password = StringUtil.readPassword();
            Credentials credentials = WalletUtils.loadCredentials(password, address);
            ProposalContractX pc = ProposalContractX.load(web3j, credentials, nodeConfigModel.getChainId(), nodeConfigModel.getHrp());
            RemoteCall<TransactionResponse> remoteCall = pc.submitProposal(proposal);
            TransactionResponse response = remoteCall.send();
            log.info(response.toString());
            return response.isStatusOk() ? Common.SUCCESS_STR : Common.FAIL_STR;
        } else {
            BaseTemplate4Serialize baseTemplate4Serialize = convert2BaseTemplate4Serialize(
                    function,
                    nodeConfigModel,
                    web3j,
                    address,
                    gasProvider.getGasLimit(),
                    gasProvider.getGasPrice(),
                    fast);
            return QrUtil.save2QrCodeImage(getQrCodeImagePrefix(), baseTemplate4Serialize);
        }
    }
}
