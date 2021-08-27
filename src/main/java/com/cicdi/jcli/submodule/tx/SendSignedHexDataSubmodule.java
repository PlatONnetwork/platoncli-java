package com.cicdi.jcli.submodule.tx;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.*;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.protocol.http.HttpService;
import lombok.extern.slf4j.Slf4j;

/**
 * 直接发送已签名16进制交易数据
 *
 * @author haypo
 * @date 2020/12/28
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "tx_sendSignedHexData", resourceBundle = "command", commandDescription = "直接发送已签名16进制交易数据")
public class SendSignedHexDataSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--hexData", "-hexData"}, description = "已签名16进制交易数据", required = true)
    protected String hexData;
    @Parameter(names = {"--fast", "-fast", "-f"}, descriptionKey = "fast")
    protected boolean fast;

    /**
     * [操作时间]:Operation：send_offline ,from：钱包文件名称，txhash：xxx ，Transaction content：{“根据解析展示具体交易模板”:””} ， blocNmuber：xxx， nodeIP: xxxxx，nodeRpcport:xxxxx
     *
     * @param jc   jc对象
     * @param argv 参数
     * @return 无, 日志输出
     * @throws Exception 一些异常
     */
    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        if (fast) {
            SendUtil.fastSendSingedData(hexData, new FastHttpService(nodeConfigModel.getRpcAddress()));
            StringUtil.info("operation: tx_sendSignedHexData, mode: fast, data: %s",
                    hexData);
        } else {
            String hash = SendUtil.sendSingedData(hexData, new HttpService(nodeConfigModel.getRpcAddress()));
            TransactionReceipt receipt = waitForTransactionReceipt(nodeConfigModel, hash);
            StringUtil.info(TransactionReceiptUtil.handleTxReceipt(receipt));
        }
        return Common.SUCCESS_STR;
    }
}
