package com.cicdi.jcli.submodule.account;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.template.BaseTemplate4Serialize;
import com.cicdi.jcli.util.*;
import com.platon.crypto.Credentials;
import com.platon.crypto.RawTransaction;
import com.platon.crypto.WalletUtils;
import com.platon.utils.Numeric;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * sign
 *
 * @author haypo
 * @date 2021/3/13
 */
@Slf4j
@SuppressWarnings("unused")
@Parameters(commandNames = "account_sign", resourceBundle = "command", commandDescription = "离线签名")
public class SignSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--address", "-address", "-d"}, description = "发送交易地址或者名称.json", required = true)
    protected String address;
    @Parameter(names = {"--data", "-data"}, description = "待签名数据，可以是待签名二维码文件、json字符串或json文件", required = true)
    protected String data;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        File datafile = new File(data);
        File addressFile = new File(address);
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        if (!addressFile.isFile()) {
            addressFile = AddressUtil.getFileFromAddress(nodeConfigModel.getHrp(), address);
        }
        String passwd = StringUtil.readPassword();
        Credentials credentials = WalletUtils.loadCredentials(passwd, addressFile);
        BaseTemplate4Serialize transferTemplate;
        if (datafile.isFile()) {
            if (datafile.getName().toLowerCase(Locale.ROOT).endsWith(Common.JSON_SUFFIX)) {
                transferTemplate = JsonUtil.readFile(datafile, BaseTemplate4Serialize.class, null);
            } else {
                transferTemplate = QrUtil.readQrCodeImage(datafile);
            }
        } else {
            transferTemplate = JSON.parseObject(data, BaseTemplate4Serialize.class);
        }
        List<String> hexValueList = new ArrayList<>();
        long flag = 0;
        for (String to : transferTemplate.getTo()) {
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    transferTemplate.getNonce().add(BigInteger.valueOf(flag++)),
                    transferTemplate.getGasPrice(),
                    transferTemplate.getGasLimit(),
                    to,
                    transferTemplate.getValue(),
                    Numeric.cleanHexPrefix(transferTemplate.getData()));
            hexValueList.add(SendUtil.signData(rawTransaction, credentials, transferTemplate.getChainId()));
        }

        log.info("签名完成。已签名数据：{}", JsonUtil.toPrettyJsonString(hexValueList));

        return Common.SUCCESS_STR;
    }
}
