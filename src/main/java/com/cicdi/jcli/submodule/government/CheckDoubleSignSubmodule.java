package com.cicdi.jcli.submodule.government;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.SlashContractX;
import com.cicdi.jcli.converter.BigIntegerConverter;
import com.cicdi.jcli.converter.CheckDoubleSignTypeConverter;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;
import com.cicdi.jcli.validator.PositiveBigIntegerValidator;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.common.DuplicateSignType;
import com.platon.protocol.Web3j;

import java.math.BigInteger;

/**
 * 查询节点是否已被举报过多签
 *
 * @author haypo
 * @date 2021/1/18
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "government_checkDoubleSign", resourceBundle = "command", commandDescriptionKey = "government.checkDoubleSign")
public class CheckDoubleSignSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--type", "-type", "-t"}, descriptionKey = "government.checkDoubleSign.type", required = true,
            converter = CheckDoubleSignTypeConverter.class)
    protected DuplicateSignType type;
    @Parameter(names = {"--nodeId", "-nodeId"}, descriptionKey = "government.checkDoubleSign.nodeId", required = true)
    protected String nodeId;
    @Parameter(names = {"--number", "-number", "-n"}, descriptionKey = "government.checkDoubleSign.number", required = true,
            converter = BigIntegerConverter.class, validateWith = PositiveBigIntegerValidator.class)
    protected BigInteger number;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);

        SlashContractX scx = SlashContractX.load(web3j, nodeConfigModel.getHrp());
        CallResponse<String> callResponse = scx.checkDoubleSign(type, nodeId, number).send();
        if (callResponse.isStatusOk()) {
            return JsonUtil.toPrettyJsonString(callResponse.getData());
        }
        return genFailString(callResponse);
    }
}
