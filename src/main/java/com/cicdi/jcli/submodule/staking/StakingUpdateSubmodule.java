package com.cicdi.jcli.submodule.staking;

import com.alaya.crypto.CipherException;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.staking.StakingUpdateTemplate;
import com.cicdi.jcli.util.contract.staking.StakingUpdateUtil;

import java.io.IOException;

/**
 * 修改质押信息
 *
 * @author haypo
 * @date 2020/12/29
 */
@Parameters(commandNames = "staking_update", commandDescription = "修改质押信息")
public class StakingUpdateSubmodule extends AbstractComplexSubmodule<StakingUpdateTemplate, StakingUpdateUtil> {

    @Override
    public FunctionUtil<StakingUpdateUtil> function() throws IOException, CipherException {
        return () -> new StakingUpdateUtil(isOnline(), address, config, param, StakingUpdateTemplate.class);
    }

    @Override
    public String generateTemplate() {
        return "类型             必填性         参数名称             参数解释\n" +
                "String          must          benefitAddress     收益账户\n" +
                "String          optional      nodeId             节点ID，若为空则使用config配置的节点id\n" +
                "String          optional      externalId         外部ID\n" +
                "String          optional      nodeName           节点名字\n" +
                "String          optional      website            节点的第三方主页\n" +
                "String          optional      details            节点的描述\n" +
                "BigInteger      must          delegateRewardPer  从佣金中获得的奖励份额的比例\n";
    }

}
