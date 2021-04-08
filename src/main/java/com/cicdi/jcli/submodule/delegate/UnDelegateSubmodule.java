package com.cicdi.jcli.submodule.delegate;

import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.delegate.DelegateUnDelegateTemplate;
import com.cicdi.jcli.util.contract.delegate.DelegateUnDelegateUtil;

/**
 * 减持/撤销委托
 *
 * @author haypo
 * @date 2021/1/8
 */
@Parameters(commandNames = "delegate_unDelegate", resourceBundle = "command", commandDescription = "减持/撤销委托")
public class UnDelegateSubmodule extends AbstractComplexSubmodule<DelegateUnDelegateTemplate, DelegateUnDelegateUtil> {

    @Override
    public FunctionUtil<DelegateUnDelegateUtil> function() {
        return () -> new DelegateUnDelegateUtil(isOnline(), address, config, param, DelegateUnDelegateTemplate.class);
    }

    @Override
    public String generateTemplate() {
        return "类型             必填性         参数名称             参数解释\n" +
                "BigInteger       must          stakingBlockNum    质押节点时的块高\n" +
                "String           optional      nodeId             节点ID\n" +
                "BigDecimal       must          amount             委托金额，单位为hrp（atp/lat）\n";
    }

}
