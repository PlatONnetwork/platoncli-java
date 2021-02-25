package com.cicdi.jcli.submodule.government;

import com.alaya.crypto.CipherException;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.government.VoteTemplate;
import com.cicdi.jcli.util.contract.government.VoteUtil;

import java.io.IOException;

/**
 * 提案投票
 *
 * @author haypo
 * @date 2021/1/14
 */
@Parameters(commandNames = "government_vote", commandDescription = "提案投票")
public class VoteSubmodule extends AbstractComplexSubmodule<VoteTemplate, VoteUtil> {
    @Override
    public FunctionUtil<VoteUtil> function() throws IOException, CipherException {
        return () -> new VoteUtil(isOnline(), address, config, param, VoteTemplate.class);
    }

    @Override
    public String generateTemplate() {
        return "类型             必填性         参数名称             参数解释\n" +
                "String          must          verifier           声明的节点，只能是验证人/候选人，若为空则默认使用config配置的节点的id\n" +
                "String          must          proposalId         提案ID，所投的提案交易的hash\n" +
                "String          must          option             投票类型；YEAS 赞成票、NAYS 反对票、ABSTENTIONS 弃权票\n";
    }
}
