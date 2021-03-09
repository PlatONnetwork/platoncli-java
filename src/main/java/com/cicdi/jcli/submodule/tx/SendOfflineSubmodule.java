package com.cicdi.jcli.submodule.tx;

import com.alaya.crypto.Credentials;
import com.alaya.crypto.RawTransaction;
import com.alaya.crypto.WalletUtils;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.utils.Numeric;
import com.alibaba.fastjson.JSON;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.service.FastHttpService;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.template.BaseTemplate4Serialize;
import com.cicdi.jcli.util.*;
import com.google.zxing.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

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
@Slf4j
@Parameters(commandNames = "tx_sendOffline", commandDescription = "发送已签名交易数据")
public class SendOfflineSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--data", "-data"}, description = "已签名的交易data数据或者代签名二维码文件", required = true)
    protected String data;
    @Parameter(names = {"--address", "-address", "-d"}, description = "发送交易地址的钱包json文件", required = true)
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
        Assert.assertTrue(new File(address).isFile());

        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        FastHttpService fastHttpService = new FastHttpService(nodeConfigModel.getRpcAddress());
        System.out.println("需要用户输入密码:");
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        Credentials credentials = WalletUtils.loadCredentials(password, address);
        BaseTemplate4Serialize fastTransferTemplate;
        File file = new File(data);
        List<String> hexValueList = new ArrayList<>();
        if (file.isFile()) {
            Result result = QrUtil.readQrCodeImage(file);
            fastTransferTemplate = JSON.parseObject(result.toString(), BaseTemplate4Serialize.class);
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
            SendUtil.fastSendSingedData(hexValueList, fastHttpService);
            log.info("operation: sendOffline, mode: fast, from: {}, toAddressesNumber: {} , transactionContent: {}, nodeRpcAddress:{}",
                    credentials.getAddress(), hexValueList.size(), "", nodeConfigModel.getRpcAddress());
        } else {
            for (String signedData : hexValueList) {
                String hash = SendUtil.sendSingedData(signedData, fastHttpService);

                Assert.assertNotNull(hash);

                TransactionReceipt receipt = waitForTransactionReceipt(nodeConfigModel, hash);
                log.info("operation: sendOffline, mode: normal, from: {}, nodeRpcAddress: {},receipt: {}",
                        credentials.getAddress(),
                        nodeConfigModel.getRpcAddress(),
                        receipt
                );
            }
        }
        return Common.SUCCESS_STR;
    }
}
