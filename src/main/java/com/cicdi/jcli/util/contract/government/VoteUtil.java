package com.cicdi.jcli.util.contract.government;

import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.enums.VoteOption;
import com.platon.crypto.CipherException;
import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.template.government.VoteTemplate;
import com.cicdi.jcli.util.NetworkParametersUtil;
import com.cicdi.jcli.util.StringUtil;
import com.cicdi.jcli.util.Web3jUtil;
import com.cicdi.jcli.util.contract.BaseContractUtil;

import java.io.IOException;

/**
 * @author haypo
 * @date 2021/1/14
 */
public class VoteUtil extends BaseContractUtil<VoteTemplate> {
    public VoteUtil(boolean isOnline, String address, String config, String param, Class<VoteTemplate> clazz) throws IOException, CipherException {
        super(isOnline, address, config, param, clazz);
    }

    @Override
    public String getTemplateSchemaPath() {
        return "/json/VoteTemplateSchema.json";
    }

    @Override
    public Function createFunction() throws IOException {
        if (StringUtil.isBlank(t.getVerifier())) {
            t.setVerifier(Web3jUtil.getNodeId(nodeConfigModel.getRpcAddress()));
        }

        return ProposalContractX.createVoteFunction(
                web3j.getProgramVersion().send().getAdminProgramVersion(),
                t.getProposalId(),
                t.getVerifier(),
                t.getOption());
    }

    @Override
    public String getPposContractAddress() {
        return NetworkParametersUtil.getPposContractAddressOfProposal(nodeConfigModel.getHrp());
    }
}
