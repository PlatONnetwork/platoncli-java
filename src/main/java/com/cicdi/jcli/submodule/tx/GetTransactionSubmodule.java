package com.cicdi.jcli.submodule.tx;

import com.platon.protocol.Web3j;
import com.platon.protocol.core.methods.response.Transaction;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.submodule.ISubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;

import java.util.Optional;

/**
 * 根据交易hash查询交易
 *
 * @author haypo
 * @date 2020/12/28
 */
@Parameters(commandNames = "tx_getTransaction",resourceBundle = "command",  commandDescription = "根据交易hash查询交易")
public class GetTransactionSubmodule extends AbstractSimpleSubmodule implements ISubmodule {
    @Parameter(names = {"--hash", "-hash", "-h"}, description = "String类型,具体查询区块hash", required = true)
    protected String hash;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel =  ConfigUtil.readConfig(config);
        FastHttpService fastHttpService = new FastHttpService(nodeConfigModel.getRpcAddress());
        Web3j web3j = Web3j.build(fastHttpService);

        Optional<Transaction> optional = web3j.platonGetTransactionByHash(hash).send().getTransaction();
        if (optional.isPresent()) {
            return JsonUtil.toPrettyJsonString(optional.get());
        } else {
            throw new RuntimeException("fail");
        }
    }
}
