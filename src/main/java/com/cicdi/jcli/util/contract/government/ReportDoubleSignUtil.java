package com.cicdi.jcli.util.contract.government;

import com.cicdi.jcli.contractx.SlashContractX;
import com.cicdi.jcli.template.government.ReportDoubleSignTemplate;
import com.cicdi.jcli.util.NetworkParametersUtil;
import com.cicdi.jcli.util.contract.BaseContractUtil;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.common.DuplicateSignType;
import com.platon.crypto.CipherException;

import java.io.IOException;

/**
 * @author haypo
 * @date 2021/1/17
 */
public class ReportDoubleSignUtil extends BaseContractUtil<ReportDoubleSignTemplate> {
    public ReportDoubleSignUtil(boolean isOnline, String address, String config, String param, Class<ReportDoubleSignTemplate> clazz) throws IOException, CipherException {
        super(isOnline, address, config, param, clazz);
    }

    @Override
    public String getTemplateSchemaPath() {
        return "/json/ReportDoubleSignTemplateSchema.json";
    }

    @Override
    public Function createFunction() throws IOException {
        DuplicateSignType duplicateSignType = t.getType();
        return SlashContractX.createReportDoubleSignFunction(duplicateSignType, t.getData());
    }

    @Override
    public String getPposContractAddress() {
        return NetworkParametersUtil.getPposContractAddressOfSlash(nodeConfigModel.getHrp());
    }
}
