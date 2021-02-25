package com.cicdi.jcli.submodule.tx;

import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.Transaction;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.submodule.ISubmodule;
import com.cicdi.jcli.util.ConvertUtil;
import com.cicdi.jcli.util.JsonUtil;

import java.util.Optional;

/**
 * 根据交易hash查询交易
 *
 * @author haypo
 * @date 2020/12/28
 */
@Parameters(commandNames = "tx_getTransaction", commandDescription = "根据交易hash查询交易")
public class GetTransactionSubmodule extends AbstractSimpleSubmodule implements ISubmodule {
    @Parameter(names = {"--hash", "-hash", "-h"}, description = "String类型,具体查询区块hash", required = true)
    protected String hash;


    /**
     * blockHash:"0xc48a3a2575a25d20cef32a43d95f8f6cbabd3a99edf6fabf73f3104d59fbeec9",
     * blockNumber: 2508,
     * from: "0x2e95e3ce0a54951eb9a99152a6d5827872dfb4fd",
     * gas: 210000,
     * gasPrice: 1000000000,
     * hash: "0x4d8e0bd8700763811b53d9047f87517ff7a63c74daa35c11a45ccae1b5ff4489",
     * input: "0xc9880000000000000000",
     * nonce: 6,
     * transactionIndex: 0,
     * value:  单位LAT
     *
     * @param receipt 回执
     * @return 字符串结果
     */
    private String transactionToString(Transaction receipt, String hrp) {
        return "\n" +
                "blockHash: " + receipt.getBlockHash() + ",\n" +
                "blockNumber: " + receipt.getBlockNumber() + ",\n" +
                "from: " + receipt.getFrom() + ",\n" +
                "to: " + receipt.getTo() + ",\n" +
                "transactionFee: " + ConvertUtil.getTxFee(receipt.getGas(), receipt.getGasPrice()) + " " + hrp + ",\n" +
                "gas: " + receipt.getGas() + ",\n" +
                "gasPrice: " + receipt.getGasPrice() + ",\n" +
                "hash: " + receipt.getHash() + ",\n" +
                "input: " + receipt.getInput() + ",\n" +
                "nonce: " + receipt.getNonce() + ",\n" +
                "transactionIndex: " + receipt.getTransactionIndex() + ",\n" +
                "value: " + ConvertUtil.von2Hrp(receipt.getValue()) + " " + hrp;
    }

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = JsonUtil.readFile(config, NodeConfigModel.class);
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
