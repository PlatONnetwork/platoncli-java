package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.model.Tuple;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.*;
import com.cicdi.jcli.validator.AddressValidator;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/**
 * 查看本地钱包
 *
 * @author haypo
 * @date 2021/3/1
 */
@Slf4j
@SuppressWarnings("unused")
@Parameters(commandNames = "account_check", resourceBundle = "command", commandDescriptionKey = "account.check")
public class CheckSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--address", "-address", "-d"}, descriptionKey = "account.check.address",
            validateWith = AddressValidator.class)
    protected String address;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtil.isBlank(address)) {
            List<Tuple<String, String>> tuples = AddressUtil.readAddressFromDir(nodeConfigModel.getHrp());
            for (Tuple<String, String> tuple : tuples) {
                stringBuilder.append(ResourceBundleUtil.getTextString("Filename")).append(": ")
                        .append(tuple.getB()).append(", address: ").append(tuple.getA()).append(".\n");
            }
        } else {
            if (JsonUtil.isJsonFile(address)) {
                String newAddress = AddressUtil.readAddressFromFile(new File(address), nodeConfigModel.getHrp());
                stringBuilder.append(ResourceBundleUtil.getTextString("Filename"))
                        .append(": ").append(address).append(", address: ").append(newAddress).append(".\n");
            } else {
                String filename = AddressUtil.getFilenameFromAddress(nodeConfigModel.getHrp(), address);
                stringBuilder.append(ResourceBundleUtil.getTextString("Filename"))
                        .append(": ").append(filename).append(", address: ").append(address).append(".\n");
            }
        }
        return stringBuilder.toString();
    }
}
