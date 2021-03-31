package com.cicdi.jcli.submodule.query;


import com.beust.jcommander.validators.PositiveInteger;
import com.cicdi.jcli.converter.BigIntegerConverter;
import com.cicdi.jcli.validator.PositiveBigIntegerValidator;
import com.github.fge.jsonschema.keyword.validator.helpers.PositiveIntegerValidator;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.methods.response.PlatonBlock;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.submodule.ISubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 根据区块块高查询区块信息
 *
 * @author haypo
 * @date 2020/12/24
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "query_getBlockByNumber", commandDescription = "根据区块块高查询区块信息")
public class GetBlockByNumberSubmodule extends AbstractSimpleSubmodule implements ISubmodule {
    @Parameter(names = {"-number", "number", "-n"}, description = "int类型，具体查询区块的块高",
            required = true,validateValueWith = PositiveBigIntegerValidator.class,
            converter = BigIntegerConverter.class
    )
    protected BigInteger number;

    private String getBlockInfo(PlatonBlock.Block block) {
        return "difficulty: " + block.getDifficultyRaw() + ",\n" +
                "extraData: " + block.getExtraData() + ",\n" +
                "gasLimit: " + block.getGasLimit() + ",\n" +
                "gasUsed: " + block.getGasUsed() + ",\n" +
                "hash: " + block.getHash() + ",\n" +
                "logsBloom: " + block.getLogsBloom() + ",\n" +
                "miner: : " + block.getMiner() + ",\n" +
                "nonce: " + block.getNonce() + ",\n" +
                "number: " + block.getNumber() + ",\n" +
                "parentHash: " + block.getParentHash() + ",\n" +
                "receiptsRoot: " + block.getReceiptsRoot() + ",\n" +
                "size: " + block.getSize() + ",\n" +
                "stateRoot: " + block.getStateRoot() + ",\n" +
                "timestamp: " + block.getTimestamp() + ",\n" +
                "totalDifficulty:" + block.getTotalDifficultyRaw() + ",\n" +
                "transactions: " + block.getTransactions() + ",\n" +
                "transactionsRoot: " + block.getTransactionsRoot();
    }


    /**
     * @param jc JCommander 对象
     * @return 区块信息
     * @throws IOException 一些异常
     */
    @Override
    public String run(JCommander jc, String... argv) throws IOException {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        DefaultBlockParameter dbp = DefaultBlockParameter.valueOf(number);
        PlatonBlock.Block block = web3j.platonGetBlockByNumber(dbp, true).send().getBlock();
        return JsonUtil.toPrettyJsonString(block);
    }

}
