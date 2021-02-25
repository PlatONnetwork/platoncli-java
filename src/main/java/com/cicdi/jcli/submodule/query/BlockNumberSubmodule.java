package com.cicdi.jcli.submodule.query;

import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.DefaultBlockParameterName;
import com.alaya.protocol.core.methods.response.PlatonBlock;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;

import java.io.IOException;

/**
 * 查询当前最高块高查询当前最高块高
 *
 * @author haypo
 * @date 2020/12/25
 */
@Parameters(commandNames = "query_blockNumber", commandDescription = "查询当前最高块高查询当前最高块高")
public class BlockNumberSubmodule extends AbstractSimpleSubmodule {
    /**
     * @param jc JCommander 对象
     * @return 区块信息
     * @throws IOException 一些异常
     */
    @Override
    public String run(JCommander jc, String... argv) throws IOException {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        PlatonBlock.Block block = web3j.platonGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
        return "BlockNumber: " + block.getNumber().toString(10);
    }

}
