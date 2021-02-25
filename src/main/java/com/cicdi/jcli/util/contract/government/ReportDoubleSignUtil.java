package com.cicdi.jcli.util.contract.government;

import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.dto.common.DuplicateSignType;
import com.alaya.crypto.CipherException;
import com.cicdi.jcli.contractx.SlashContractX;
import com.cicdi.jcli.template.government.ReportDoubleSignTemplate;
import com.cicdi.jcli.util.NetworkParametersUtil;
import com.cicdi.jcli.util.contract.BaseContractUtil;

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
    public Function createFunction() throws IOException {
        DuplicateSignType duplicateSignType = t.getType();
        return SlashContractX.createReportDoubleSignFunction(duplicateSignType, t.getData());
    }

    @Override
    public String getPposContractAddress() {
        return NetworkParametersUtil.getPposContractAddressOfSlash(nodeConfigModel.getHrp());
    }
}
