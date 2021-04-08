package com.cicdi.jcli.submodule.query;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.methods.response.PlatonBlock;

/**
 * 根据区块hash查询区块信息
 *
 * @author haypo
 * @date 2021/1/18
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "query_getBlockByHash", resourceBundle = "command", commandDescription = "根据区块hash查询区块信息")
public class GetBlockByHashSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--hash", "-hash", "-h"}, descriptionKey = "query.getBlockByHash.hash")
    protected String hash;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        PlatonBlock block = web3j.platonGetBlockByHash(hash, true).send();
        return JsonUtil.toPrettyJsonString(block.getBlock());
    }
}
