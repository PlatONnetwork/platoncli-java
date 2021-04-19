package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.validators.PositiveInteger;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.Locale;

/**
 * 创建钱包
 *
 * @author haypo
 * @date 2021/3/1
 */
@Slf4j
@SuppressWarnings("unused")
@Parameters(commandNames = "account_new", resourceBundle = "command", commandDescriptionKey = "account.new")
public class NewSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--name", "-name", "-n"}, descriptionKey = "account.new.name")
    protected String name;
    @Parameter(names = {"--hrp", "-hrp", "-h"}, descriptionKey = "account.new.hrp")
    protected String hrp;
    @Parameter(names = {"--batch", "-batch", "-b"}, descriptionKey = "account.new.batch",
            validateWith = PositiveInteger.class)
    protected int batch;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        if (batch == 0 && StringUtil.isBlank(name)) {
            throw new ParameterException(ResourceBundleUtil.getTextString("NewSubmodule.text1"));
        }
        if (name != null) {
            if (!name.toLowerCase(Locale.ROOT).endsWith(Common.JSON_SUFFIX)) {
                name += ".json";
            }
        }
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        if (hrp == null) {
            System.out.println(ResourceBundleUtil.getTextString("NewSubmodule.text2"));
            hrp = nodeConfigModel.getHrp();
        }
        if (batch == 0) {
            File file = new File(name);
            if (!file.exists()) {
                String passwd = StringUtil.readPassword();
                if (WalletUtil.createWalletFile(passwd, file, hrp)) {
                    return Common.SUCCESS_STR;
                }
            } else {
                //重复钱包不会覆盖
                throw new FileAlreadyExistsException(name);
            }
        }
        if (batch >= 0) {
            String passwd = StringUtil.readPassword();
            name = "address";
            int index = 0;
            while (index < batch) {
                File file = new File(name + index + ".json");
                int repeatFlag = 0;
                while (file.exists()) {
                    repeatFlag++;
                    file = new File(name + index + "(" + repeatFlag + ").json");
                }
                WalletUtil.createWalletFile(passwd, file, hrp);
                index++;
            }
            return Common.SUCCESS_STR;
        }
        return Common.FAIL_STR;
    }
}
