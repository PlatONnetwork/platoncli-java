package com.cicdi.jcli.submodule.query;

import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.PlatonBlock;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;

/**
 * 根据区块hash查询区块信息
 *
 * @author haypo
 * @date 2021/1/18
 */
@Parameters(commandNames = "query_getBlockByHash", commandDescription = "根据区块hash查询区块信息")
public class GetBlockByHashSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--hash", "-hash", "-h"}, description = "具体查询区块hash")
    protected String hash;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        PlatonBlock block = web3j.platonGetBlockByHash(hash, true).send();
        return JsonUtil.toPrettyJsonString(block.getBlock());
    }
}
