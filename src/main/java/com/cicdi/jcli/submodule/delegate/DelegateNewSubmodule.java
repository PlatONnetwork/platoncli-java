package com.cicdi.jcli.submodule.delegate;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.delegate.DelegateNewTemplate;
import com.cicdi.jcli.util.contract.delegate.DelegateNewUtil;
import com.platon.crypto.CipherException;

import java.io.IOException;

/**
 * 委托
 *
 * @author haypo
 * @date 2021/1/4
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "delegate_new", commandDescription = "委托")
public class DelegateNewSubmodule extends AbstractComplexSubmodule<DelegateNewTemplate, DelegateNewUtil> {

    @Override
    public FunctionUtil<DelegateNewUtil> function() throws IOException, CipherException {
        return () -> new DelegateNewUtil(isOnline(), address, config, param, DelegateNewTemplate.class);
    }

    @Override
    public String generateTemplate() {
        return "类型             必填性         参数名称          参数解释\n" +
                "Integer         must          type             余额类型，0: 自由金额，1: 锁仓金额\n" +
                "String          optional      nodeId           节点ID\n" +
                "BigInteger      must          amount           委托金额\n";
    }
}
