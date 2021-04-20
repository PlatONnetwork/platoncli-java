package com.cicdi.jcli.submodule.account;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.template.BaseTemplate4Serialize;
import com.cicdi.jcli.util.*;
import com.cicdi.jcli.validator.AddressValidator;
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
@Parameters(commandNames = "account_sign", resourceBundle = "command", commandDescriptionKey = "account.sign")
public class SignSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--address", "-address", "-d"}, descriptionKey = "address", required = true,
            validateWith = AddressValidator.class)
    protected String address;
    @Parameter(names = {"--data", "-data"}, descriptionKey = "account.sign.data", required = true)
    protected String data;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        File addressFile = new File(address);
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        if (!addressFile.isFile()) {
            addressFile = AddressUtil.getFileFromAddress(nodeConfigModel.getHrp(), address);
        }
        String passwd = StringUtil.readPassword();
        Credentials credentials = WalletUtils.loadCredentials(passwd, addressFile);
        BaseTemplate4Serialize transferTemplate;
        if (JsonUtil.isJsonFile(data)) {
            File datafile = new File(data);
            if (datafile.getName().toLowerCase(Locale.ROOT).endsWith(Common.JSON_SUFFIX)) {
                transferTemplate = JsonUtil.readFile(datafile, BaseTemplate4Serialize.class,
                        JsonUtil.readJsonSchemaFromResource("/json/BaseTemplate4DeserializeSchema.json"));
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

        log.info("{}: {}", ResourceBundleUtil.getTextString("SignSubmodule.text1"),
                JsonUtil.toPrettyJsonString(hexValueList));

        return Common.SUCCESS_STR;
    }
}
