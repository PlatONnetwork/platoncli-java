package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.model.WalletFileX;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.*;
import com.cicdi.jcli.validator.AddressValidator;
import com.platon.crypto.CipherException;
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
@SuppressWarnings("unused")
@Parameters(commandNames = "account_modify", resourceBundle = "command", commandDescriptionKey = "account.modify")
public class ModifySubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--address", "-address", "-d"}, descriptionKey = "account.modify.address", required = true,
            validateWith = AddressValidator.class)
    protected String address;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        File file = new File(address);
        if (!file.isFile()) {
            file = AddressUtil.getFileFromAddress(nodeConfigModel.getHrp(), address);
            StringUtil.info("%s:%s", ResourceBundleUtil.getTextString("foundWalletFile"), file.getName());
        }
        String password = StringUtil.readPassword();
        Credentials credentials = WalletUtils.loadCredentials(password, file);
        if (credentials.getEcKeyPair().getPrivateKey() == null) {
            password = StringUtil.readPassword();
            credentials = WalletUtils.loadCredentials(password, file);
            if (credentials.getEcKeyPair().getPrivateKey() == null) {
                throw new CipherException("Invalid password provided");
            }
        }

        String newPassword = StringUtil.readNewPasswordTwice();
        WalletFile newWalletFile = WalletUtil.genWalletByPrivateKey(credentials.getEcKeyPair().getPrivateKey().toString(16), newPassword);
        String newWalletFilename = "new." + file.getName();
        newWalletFilename = JsonUtil.writeJsonFileWithNoConflict(newWalletFilename, newWalletFile);

        String oldBipWalletName = "Bip39-" + file.getName();
        File oldBipWalletFile = new File("wallet/" + oldBipWalletName);
        if (oldBipWalletFile.isFile() && oldBipWalletFile.exists()) {
            WalletFileX walletFileX = JsonUtil.readFile(oldBipWalletFile, WalletFileX.class,
                    JsonUtil.readJsonSchemaFromResource("/json/WalletFileXSchema.json"));
            walletFileX.setFilename(newWalletFilename);
            String finalFilename = JsonUtil.writeJsonFileWithNoConflict("wallet/Bip39-" + newWalletFilename, walletFileX);
            StringUtil.info("%s:%s", ResourceBundleUtil.getTextString("createMnemonicBackupFile"), finalFilename);
        } else {
            StringUtil.warn("%s", ResourceBundleUtil.getTextString("mnemonicBackupFileNotFound"));
        }

        StringUtil.info("%s: %s", newWalletFilename, ResourceBundleUtil.getTextString("ModifySubmodule.text1"));
        String select = new Scanner(System.in).nextLine().toLowerCase(Locale.ROOT);
        switch (select) {
            case "y":
                StringUtil.info(ResourceBundleUtil.getTextString("ModifySubmodule.text2"));
                select = new Scanner(System.in).nextLine().toLowerCase(Locale.ROOT);
                switch (select) {
                    case "m":
                        return BackupsSubmodule.backupMnemonic(password, credentials, newWalletFilename);
                    case "p":
                        return BackupsSubmodule.backupPrivateKey(WalletUtils.loadCredentials(newPassword, newWalletFilename), newWalletFilename);
                    default:
                        StringUtil.info(ResourceBundleUtil.getTextString("ModifySubmodule.text3"));
                        return BackupsSubmodule.backupMnemonic(newPassword, credentials, newWalletFilename);
                }
            case "n":
                return Common.SUCCESS_STR + ": " + ResourceBundleUtil.getTextString("ModifySubmodule.text4");
            default:
                return ResourceBundleUtil.getTextString("ModifySubmodule.text5");
        }
    }
}
