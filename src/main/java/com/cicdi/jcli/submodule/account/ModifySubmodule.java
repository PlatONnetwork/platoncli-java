package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.model.WalletFileX;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.*;
import com.platon.crypto.Credentials;
import com.platon.crypto.WalletFile;
import com.platon.crypto.WalletUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Locale;
import java.util.Scanner;

/**
 * 修改钱包密码
 *
 * @author haypo
 * @date 2021/3/13
 */
@Slf4j
@SuppressWarnings("unused")
@Parameters(commandNames = "account_modify", resourceBundle = "command", commandDescriptionKey = "account.modify")
public class ModifySubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--address", "-address", "-d"}, descriptionKey = "account.modify.address", required = true)
    protected String address;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        File file = new File(address);
        if (!file.isFile()) {
            file = AddressUtil.getFileFromAddress(nodeConfigModel.getHrp(), address);
            log.info("已找到钱包文件：{}", file.getName());
        }
        String password = StringUtil.readPassword();
        Credentials credentials = WalletUtils.loadCredentials(password, file);
        if (credentials.getEcKeyPair().getPrivateKey() == null) {
            password = StringUtil.readPassword();
            credentials = WalletUtils.loadCredentials(password, file);
            if (credentials.getEcKeyPair().getPrivateKey() == null) {
                throw new RuntimeException("密码输入错误");
            }
        }

        String newPassword = StringUtil.readNewPasswordTwice();
        WalletFile newWalletFile = WalletUtil.genWalletByPrivateKey(credentials.getEcKeyPair().getPrivateKey().toString(16), newPassword);
        String newWalletFilename = "new." + file.getName();
        newWalletFilename = JsonUtil.writeJsonFileWithNoConflict(newWalletFilename, newWalletFile);

        String oldBipWalletName = "Bip39-" + file.getName();
        File oldBipWalletFile = new File("wallet/" + oldBipWalletName);
        if (oldBipWalletFile.isFile() && oldBipWalletFile.exists()) {
            WalletFileX walletFileX = JsonUtil.readFile(oldBipWalletFile, WalletFileX.class, null);
            walletFileX.setFilename(newWalletFilename);
            String finalFilename = JsonUtil.writeJsonFileWithNoConflict("wallet/Bip39-" + newWalletFilename, walletFileX);
            log.info("已创建助记词密文文件：{}", finalFilename);
        } else {
            log.warn("找不到原钱包的助记词记录");
        }


        log.info("已更新钱包文件：{}是否备份？Y/N", newWalletFilename);
        String select = new Scanner(System.in).nextLine().toLowerCase(Locale.ROOT);
        switch (select) {
            case "y":
                log.info("备份助记词请输入m，备份私钥请输入p");
                select = new Scanner(System.in).nextLine().toLowerCase(Locale.ROOT);
                switch (select) {
                    case "m":
                        return BackupsSubmodule.backupMnemonic(newPassword, credentials, newWalletFilename);
                    case "p":
                        return BackupsSubmodule.backupPrivateKey(WalletUtils.loadCredentials(newPassword, newWalletFilename), newWalletFilename);
                    default:
                        log.info("输入异常，默认备份助记词");
                        return BackupsSubmodule.backupMnemonic(newPassword, credentials, newWalletFilename);
                }
            case "n":
                log.info("暂不备份");
                return Common.SUCCESS_STR;
            default:
                throw new RuntimeException("输入异常，暂不备份");
        }
    }
}
