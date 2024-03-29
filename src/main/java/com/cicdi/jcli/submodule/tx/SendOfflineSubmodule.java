package com.cicdi.jcli.submodule.tx;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.template.BaseTemplate4Serialize;
import com.cicdi.jcli.util.*;
import com.platon.crypto.Credentials;
import com.platon.crypto.RawTransaction;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.protocol.http.HttpService;
import com.platon.utils.Numeric;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 发送已签名交易数据
 *
 * @author haypo
 * @date 2020/12/28
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "tx_sendOffline", resourceBundle = "command", commandDescriptionKey = "tx.sendOffline")
public class SendOfflineSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--data", "-data"}, descriptionKey = "tx.sendOffline.data", required = true)
    protected String data;
    @Parameter(names = {"--address", "-address", "-d"}, descriptionKey = "address", required = true)
    protected String address;

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
        File addressFile = AddressUtil.getFileFromAddress(nodeConfigModel.getHrp(), address);

        String password = StringUtil.readPassword();
        Credentials credentials = WalletUtil.loadCredentials(password, addressFile, nodeConfigModel.getHrp());
        BaseTemplate4Serialize fastTransferTemplate;
        File file = new File(data);
        List<String> hexValueList = new ArrayList<>();
        if (file.isFile()) {
            fastTransferTemplate = QrUtil.readQrCodeImage(file);
        } else {
            fastTransferTemplate = JSON.parseObject(data, BaseTemplate4Serialize.class);
        }

        long flag = 0;
        //对每个目的地址,生成已签名交易
        for (String to : fastTransferTemplate.getTo()) {
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    fastTransferTemplate.getNonce().add(BigInteger.valueOf(flag++)),
                    fastTransferTemplate.getGasPrice(),
                    fastTransferTemplate.getGasLimit(),
                    to,
                    fastTransferTemplate.getValue(),
                    Numeric.cleanHexPrefix(fastTransferTemplate.getData()));
            hexValueList.add(SendUtil.signData(rawTransaction, credentials, fastTransferTemplate.getChainId()));
        }
        if (fastTransferTemplate.isFast()) {
            SendUtil.fastSendSingedData(hexValueList, new FastHttpService(nodeConfigModel.getRpcAddress()));
            StringUtil.info("operation: sendOffline, mode: fast, from: %s, toAddressesNumber: %s , transactionContent: %s, nodeRpcAddress:%s",
                    credentials.getAddress(), hexValueList.size(), "", nodeConfigModel.getRpcAddress());
        } else {
            for (String signedData : hexValueList) {
                String hash = SendUtil.sendSingedData(signedData, new HttpService(nodeConfigModel.getRpcAddress()));

                TransactionReceipt receipt = waitForTransactionReceipt(nodeConfigModel, hash);
                StringUtil.info(TransactionReceiptUtil.handleTxReceipt(receipt));
            }
        }
        return Common.SUCCESS_STR;
    }
}
