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
@Parameters(commandNames = "government_submitProposal", resourceBundle = "command", commandDescriptionKey = "government.submitProposal")
public class SubmitProposalSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--offline", "-o"}, descriptionKey = "offline")
    protected boolean offline;
    @Parameter(names = {"--template", "-template", "-t"}, help = true, descriptionKey = "template")
    protected boolean template;
    @Parameter(names = {"--param", "-param", "-p"}, descriptionKey = "param", required = true)
    protected String param;
    @Parameter(names = {"--address", "-address", "-d"}, descriptionKey = "AbstractComplexSubmodule.address", required = true)
    protected String address;
    @Parameter(names = {"--module", "-module", "-m"}, descriptionKey = "government.submitProposal.module", required = true)
    protected String module;
    @Parameter(names = {"--fast", "-fast", "-f"}, descriptionKey = "fast")
    protected boolean fast;

    public boolean isOnline() {
        return !offline && new File(address).isFile();
    }

    public String getTemplateInfo() {
        System.out.println("TextProposalTemplate:");
        ResourceBundleUtil.printTemplate("TextProposalTemplate");
        System.out.println("CancelProposalTemplate:");
        ResourceBundleUtil.printTemplate("CancelProposalTemplate");
        System.out.println("ParamProposalTemplate:");
        ResourceBundleUtil.printTemplate("ParamProposalTemplate");
        System.out.println("VersionProposalTemplate:");
        ResourceBundleUtil.printTemplate("VersionProposalTemplate");
        return Common.SUCCESS_STR;
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
