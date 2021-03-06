package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.StringUtil;
import com.cicdi.jcli.util.WalletUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;

/**
 * 创建钱包
 *
 * @author haypo
 * @date 2021/3/1
 */
@Slf4j
@SuppressWarnings("unused")
@Parameters(commandNames = "account_new", commandDescription = "创建钱包")
public class NewSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--name", "-name", "-n"}, description = "钱包名字，如wallet.json")
    protected String name;
    @Parameter(names = {"--hrp", "-hrp", "-h"}, description = "网络类型")
    protected String hrp;
    @Parameter(names = {"--batch", "-batch", "-b"}, description = "批量生成钱包")
    protected int batch;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        if (batch < 0) {
            throw new ParameterException("batch is not correct");
        }
        if (batch == 0 && StringUtil.isBlank(name)) {
            throw new ParameterException("name can not be blank");
        }
        if (batch == 0) {
            File file = new File(name);
            if (!file.exists()) {
                String passwd = StringUtil.readPassword();
                if (WalletUtil.createWalletFile(passwd, file, true)) {
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
                WalletUtil.createWalletFile(passwd, file, true);
                index++;
            }
            return Common.SUCCESS_STR;
        }
        return Common.FAIL_STR;
    }
}
