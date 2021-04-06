package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.AddressUtil;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.WalletUtil;
import com.cicdi.jcli.validator.AddressValidator;
import com.platon.crypto.Credentials;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Scanner;

import static com.cicdi.jcli.util.StringUtil.readPassword;

/**
 * 删除钱包
 *
 * @author haypo
 * @date 2021/3/1
 */
@Slf4j
@SuppressWarnings("unused")
@Parameters(commandNames = "account_delete", commandDescription = "删除钱包")
public class DeleteSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--address", "-address", "-d"}, required = true,
            description = "指定钱包文件或者钱包文件地址删除对应钱包文件", validateValueWith = AddressValidator.class)
    protected String address;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        File f1 = new File(address);

        String hrp = ConfigUtil.readConfig(config).getHrp();
        if (!f1.isFile()) {
            f1 = AddressUtil.getFileFromAddress(hrp, address);
            log.info("已找到钱包文件：{}", f1.getName());
        }
        String passwd = readPassword();
        Credentials credentials = WalletUtil.loadCredentials(passwd, f1, hrp);

        boolean deleteResult = false;
        if (credentials.getEcKeyPair().getPrivateKey() != null) {
            System.out.println("Do you want to delete the wallet file: " + f1.getName() + " and it's mnemonic backup file? Y/N");
            String s = new Scanner(System.in).nextLine();
            if (Common.LETTER_Y.equalsIgnoreCase(s)) {
                File mnemonicFile = new File("wallet/Bip39-" + f1.getName());
                if (mnemonicFile.delete()) {
                    log.info("mnemonic backup file has been deleted");
                } else {
                    log.warn("mnemonic backup file delete failed");
                }
                deleteResult = f1.delete();
            }
        }
        return deleteResult ? Common.SUCCESS_STR + ": " + f1.getName() + " is deleted" :
                Common.FAIL_STR + ": " + f1.getName() + " delete failed";
    }
}
