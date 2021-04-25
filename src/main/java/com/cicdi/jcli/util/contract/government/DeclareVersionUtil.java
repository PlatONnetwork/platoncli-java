package com.cicdi.jcli.util.contract.government;

import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.model.Tuple;
import com.cicdi.jcli.template.government.DeclareVersionTemplate;
import com.cicdi.jcli.util.NetworkParametersUtil;
import com.cicdi.jcli.util.StringUtil;
import com.cicdi.jcli.util.Web3jUtil;
import com.cicdi.jcli.util.contract.BaseContractUtil;
import com.platon.contracts.ppos.abi.Function;

import java.io.IOException;

/**
 * @author haypo
 * @date 2021/1/4
 */
public class DeclareVersionUtil extends BaseContractUtil<DeclareVersionTemplate> {

    public DeclareVersionUtil(boolean isOnline, String jsonPath, String config, String param, Class<DeclareVersionTemplate> clazz) throws Exception {
        super(isOnline, jsonPath, config, param, clazz);
    }

    @Override
    public String getTemplateSchemaPath() {
        return "/json/DeclareVersionTemplateSchema.json";
    }

    @Override
    public Tuple<Boolean, String> verifyParam() {
        return passVerifyParam;
    }

    @Override
    public Function createFunction() throws IOException {
        if (StringUtil.isBlank(t.getActiveNode())) {
            t.setActiveNode(Web3jUtil.getNodeId(nodeConfigModel.getRpcAddress()));
        }
        return ProposalContractX.createDeclareVersionFunction(web3j.getProgramVersion().send().getAdminProgramVersion(), t.getActiveNode());
    }

    @Override
    public String getPposContractAddress() {
        return NetworkParametersUtil.getPposContractAddressOfProposal(nodeConfigModel.getHrp());
    }


}
