package com.cicdi.jcli.submodule.government;

import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.protocol.Web3j;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.ProposalContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;
import org.junit.Assert;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询提案的累计可投票人数
 *
 * @author haypo
 * @date 2021/1/17
 */
@Parameters(commandNames = "government_getAccuVerifiersCount", commandDescription = "查询提案的累计可投票人数")
public class GetAccuVerifiersCountSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--pid", "-pid"}, description = "查询的提案的id", required = true)
    protected String proposalId;
    @Parameter(names = {"--hash", "-hash", "-h"}, description = "当前最新区块的hash", required = true)
    protected String blockHash;

    /**
     * voteTurnout:0,累积可投票人数，BigInteger
     * Yeas:0,赞成票数，BigInteger
     * Nays:0,反对票数，BigInteger
     * Abstentions:0,弃权票数，BigInteger
     *
     * @param data 初始查询结果
     * @return 解析后的结果
     */
    private Map<String, BigInteger> toMap(List<BigInteger> data) {
        Assert.assertEquals(data.size(), 4);
        return new LinkedHashMap<String, BigInteger>(8) {
            {
                put("voteTurnout", data.get(0));
                put("Yeas", data.get(1));
                put("Nays", data.get(2));
                put("Abstentions", data.get(3));
            }
        };
    }

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        ProposalContractX pcx = ProposalContractX.load(web3j, nodeConfigModel.getHrp());
        CallResponse<List<BigInteger>> callResponse = pcx.getAccuVerifiersCount(proposalId, blockHash).send();
        if (callResponse.isStatusOk()) {
            return JsonUtil.toPrettyJsonString(toMap(callResponse.getData()));
        }
        return genFailString(callResponse);
    }
}
