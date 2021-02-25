package com.cicdi.jcli.submodule.tx;

import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.JsonUtil;

import java.util.Optional;

/**
 * 根据交易hash查询交易信息
 *
 * @author haypo
 * @date 2020/12/28
 */
@Parameters(commandNames = "tx_getTransactionReceipt", commandDescription = "根据交易hash查询交易信息")
public class GetTransactionReceiptSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--hash", "-hash", "-h"}, description = "String类型,具体查询区块hash", required = true)
    protected String hash;


    /**
     * blockHash: "0xc48a3a2575a25d20cef32a43d95f8f6cbabd3a99edf6fabf73f3104d59fbeec9",
     * blockNumber: 2508,
     * contractAddress: null,
     * cumulativeGasUsed: 21168,
     * from: "0x2e95e3ce0a54951eb9a99152a6d5827872dfb4fd",
     * gasUsed: 21168,
     * logs: [],
     * status: success/fail,
     * to: "0xceca295e1471b3008d20b017c7df7d4f338a7fba",
     * transactionHash: "0x4d8e0bd8700763811b53d9047f87517ff7a63c74daa35c11a45ccae1b5ff4489",
     * transactionIndex: 0
     *
     * @param receipt 回执
     * @return 字符串结果
     */
    private String receiptToString(TransactionReceipt receipt) {
        return "blockHash:" + receipt.getBlockHash() + ",\n"
                + "blockNumber:" + receipt.getBlockNumber() + ",\n"
                + "contractAddress:" + receipt.getContractAddress() + ",\n"
                + "cumulativeGasUsed:" + receipt.getCumulativeGasUsed() + ",\n"
                + "from:" + receipt.getFrom() + ",\n"
                + "gasUsed:" + receipt.getGasUsed() + ",\n"
                + "logs:" + receipt.getLogs() + ",\n"
                + "status:" + getStatus(receipt) + ",\n"
                + "to:" + receipt.getTo() + ",\n"
                + "transactionIndex:" + receipt.getTransactionIndex() + ",\n"
                + "transactionHash:" + receipt.getTransactionHash()
                ;
    }

    private String getStatus(TransactionReceipt receipt) {
        return "0x1".equals(receipt.getStatus()) ? "success" : "fail";
    }

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = JsonUtil.readFile(config, NodeConfigModel.class);
        Web3j web3j = Web3j.build(new FastHttpService(nodeConfigModel.getRpcAddress()));
        Optional<TransactionReceipt> optional = web3j.platonGetTransactionReceipt(hash).send().getTransactionReceipt();
        if (optional.isPresent()) {
            return JsonUtil.toPrettyJsonString(optional.get());
        } else {
            throw new RuntimeException("fail");
        }
    }


}
