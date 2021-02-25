package com.cicdi.jcli.submodule.government;

import com.alaya.contracts.ppos.dto.CallResponse;
import com.alaya.contracts.ppos.dto.common.DuplicateSignType;
import com.alaya.protocol.Web3j;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.SlashContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;

import java.math.BigInteger;

/**
 * 查询节点是否已被举报过多签
 *
 * @author haypo
 * @date 2021/1/18
 */
@Parameters(commandNames = "government_checkDoubleSign", commandDescription = "查询节点是否已被举报过多签")
public class CheckDoubleSignSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--type", "-type", "-t"}, description = "双签类型：1:PREPARE_BLOCK; 2:PREPARE_VOTE; 3:VIEW_CHANGE", required = true)
    protected String type;
    @Parameter(names = {"--nodeId", "-nodeId"}, description = "举报的节点id", required = true)
    protected String nodeId;
    @Parameter(names = {"--number", "-number", "-n"}, description = "多签的块高", required = true)
    protected String number;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);

        SlashContractX scx = SlashContractX.load(web3j, nodeConfigModel.getHrp());
        CallResponse<String> callResponse = scx.checkDoubleSign(DuplicateSignType.valueOf(type), nodeId, new BigInteger(number)).send();
        if (callResponse.isStatusOk()) {
            return JsonUtil.toPrettyJsonString(callResponse.getData());
        }
        return genFailString(callResponse);
    }
}
