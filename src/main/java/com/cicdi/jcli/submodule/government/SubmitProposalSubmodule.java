package com.cicdi.jcli.submodule.government;

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
import com.cicdi.jcli.template.government.TextProposalTemplate;
import com.cicdi.jcli.template.government.VersionProposalTemplate;
import com.cicdi.jcli.util.*;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.resp.Proposal;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.tx.gas.GasProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

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
    @Parameter(names = {"--module", "-module", "-m"}, description = "提案类型, 包括: CancelProposal(取消提案), ParamProposal(参数提案), VersionProposal(升级提案), TextProposal(文本提案)", required = true)
    protected String module;
    @Parameter(names = {"--fast", "-fast", "-f"}, description = "是否使用快速发送功能，默认不使用")
    protected boolean fast;

    public boolean isOnline() {
        return !offline && new File(address).isFile();
    }

    public String getTemplateInfo() {
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
                "BigInteger      must          newVersion          升级版本\n" +
                "TextProposalTemplate:\n" +
                "类型             必填性         参数名称             参数解释\n" +
                "String          must          verifier            提交提案的验证人，nodeId\n" +
                "String          must          piPid               PIPID\n";
    }

    public Proposal getProposalByModule(String module) throws IOException {
        Proposal proposal;
        switch (module) {
            case "cancel_proposal":
            case "cancelProposal":
            case "CancelProposal":
                CancelProposalTemplate cancelProposalTemplate = ParamUtil.readParam(param, CancelProposalTemplate.class,
                        JsonUtil.readJsonSchemaFromResource("/json/CancelProposalTemplateSchema.json"));
                proposal = Proposal.createSubmitCancelProposalParam(cancelProposalTemplate.getVerifier(), cancelProposalTemplate.getPiPid(), cancelProposalTemplate.getEndVotingRound(), cancelProposalTemplate.getCanceledProposalId());
                break;
            case "param_proposal":
            case "paramProposal":
            case "ParamProposal":
                ParamProposalTemplate paramProposalTemplate = ParamUtil.readParam(param, ParamProposalTemplate.class,
                        JsonUtil.readJsonSchemaFromResource("/json/ParamProposalTemplateSchema.json"));
                proposal = Proposal.createSubmitParamProposalParam(paramProposalTemplate.getVerifier(), paramProposalTemplate.getPiPid(), paramProposalTemplate.getModule(), paramProposalTemplate.getName(), paramProposalTemplate.getNewValue());
                break;
            case "version_proposal":
            case "versionProposal":
            case "VersionProposal":
                VersionProposalTemplate versionProposalTemplate = ParamUtil.readParam(param, VersionProposalTemplate.class,
                        JsonUtil.readJsonSchemaFromResource("/json/VersionProposalTemplateSchema.json"));
                proposal = Proposal.createSubmitVersionProposalParam(versionProposalTemplate.getVerifier(), versionProposalTemplate.getPiPid(), versionProposalTemplate.getNewVersion(), versionProposalTemplate.getEndVotingRound());
                break;
            case "TextProposal":
            case "text_proposal":
            case "textProposal":
                TextProposalTemplate textProposalTemplate = ParamUtil.readParam(param, TextProposalTemplate.class,
                        JsonUtil.readJsonSchemaFromResource("/json/TextProposalTemplateSchema.json"));
                proposal = Proposal.createSubmitTextProposalParam(textProposalTemplate.getVerifier(), textProposalTemplate.getPiPid());
                break;
            default:
                throw new RuntimeException("module parameter error!");
        }
        return proposal;
    }

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        if (template && argv.length == Common.TWO) {
            return getTemplateInfo();
        }

        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = Web3j.build(new FastHttpService(nodeConfigModel.getRpcAddress()));
        Proposal proposal = getProposalByModule(module);

        Function function = ProposalContractX.createSubmitProposalFunction(proposal);
        GasProvider gasProvider = Common.getDefaultGasProvider(function);
        if (isOnline()) {
            String password = StringUtil.readPassword();
            Credentials credentials = WalletUtil.loadCredentials(password, address, nodeConfigModel.getHrp());

            String txHash = SendUtil.send(
                    nodeConfigModel.getHrp(),
                    NetworkParametersUtil.getPposContractAddressOfProposal(nodeConfigModel.getHrp()),
                    EncoderUtils.functionEncoder(function),
                    BigInteger.ZERO,
                    gasProvider.getGasPrice(),
                    gasProvider.getGasLimit(),
                    web3j,
                    credentials,
                    nodeConfigModel.getChainId()
            );

            return Common.SUCCESS_STR + ": " + txHash;
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
