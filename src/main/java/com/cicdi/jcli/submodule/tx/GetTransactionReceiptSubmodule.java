package com.cicdi.jcli.submodule.tx;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.TransactionReceiptUtil;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.methods.response.TransactionReceipt;

import java.util.Optional;

/**
 * 根据交易hash查询交易信息
 *
 * @author haypo
 * @date 2020/12/28
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "tx_getTransactionReceipt", commandDescription = "根据交易hash查询交易信息")
public class GetTransactionReceiptSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--hash", "-hash", "-h"}, description = "String类型,具体查询区块hash", required = true)
    protected String hash;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = Web3j.build(new FastHttpService(nodeConfigModel.getRpcAddress()));
        Optional<TransactionReceipt> optional = web3j.platonGetTransactionReceipt(hash).send().getTransactionReceipt();
        if (optional.isPresent()) {
            return TransactionReceiptUtil.handleTxReceipt(optional.get());
        } else {
            throw new RuntimeException("fail");
        }
    }

}
