package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.StringUtil;
import com.cicdi.jcli.util.WalletUtil;
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
@Parameters(commandNames = "account_new", commandDescription = "创建钱包，同时在wallet文件夹下创建助记词的密文文件，请勿删除，否则无法备份！")
public class NewSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--name", "-name", "-n"}, description = "钱包名字，如wallet.json")
    protected String name;
    @Parameter(names = {"--hrp", "-hrp", "-h"}, description = "网络类型")
    protected String hrp;
    @Parameter(names = {"--batch", "-batch", "-b"}, description = "批量生成钱包，自然数")
    protected int batch;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        if (batch < 0) {
            throw new ParameterException("batch参数非法");
        }
        if (batch == 0 && StringUtil.isBlank(name)) {
            throw new ParameterException("生成单个钱包时需要指定名称");
        }
        if (name != null) {
            if (!name.toLowerCase(Locale.ROOT).endsWith(Common.JSON_SUFFIX)) {
                name += ".json";
            }
        }
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        if (hrp == null) {
            System.out.println("未提供hrp参数，默认从节点配置读取");
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
