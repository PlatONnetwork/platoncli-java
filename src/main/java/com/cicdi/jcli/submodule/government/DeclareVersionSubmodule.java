package com.cicdi.jcli.submodule.government;

import com.platon.crypto.CipherException;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.government.DeclareVersionTemplate;
import com.cicdi.jcli.util.contract.government.DeclareVersionUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 版本声明
 *
 * @author haypo
 * @date 2020/12/31
 */
@Slf4j
@Parameters(commandNames = "government_declareVersion", commandDescription = "版本声明")
public class DeclareVersionSubmodule extends AbstractComplexSubmodule<DeclareVersionTemplate, DeclareVersionUtil> {
    @Override
    public FunctionUtil<DeclareVersionUtil> function() throws IOException, CipherException {
        return () -> new DeclareVersionUtil(isOnline(), address, config, param, DeclareVersionTemplate.class);
    }

    @Override
    public String generateTemplate() {
        return "类型        必填性         参数名称          参数解释\n" +
                "String     must          activeNode       声明的节点id，只能是验证人/候选人。若为空，则默认使用config配置的节点的id\n";
    }
}
