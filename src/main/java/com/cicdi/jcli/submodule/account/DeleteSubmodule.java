package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.*;
import com.cicdi.jcli.validator.AddressValidator;
import com.platon.crypto.Credentials;

import java.io.File;

import static com.cicdi.jcli.util.StringUtil.readPassword;

/**
 * 删除钱包
 *
 * @author haypo
 * @date 2021/3/1
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "account_delete", resourceBundle = "command", commandDescriptionKey = "account.delete")
public class DeleteSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--address", "-address", "-d"}, required = true,
            descriptionKey = "account.delete.address", validateWith = AddressValidator.class)
    protected String address;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        File f1 = new File(address);

        String hrp = ConfigUtil.readConfig(config).getHrp();
        if (!f1.isFile()) {
            f1 = AddressUtil.getFileFromAddress(hrp, address);
            StringUtil.info("%s: %s", ResourceBundleUtil.getTextString("foundWalletFile"), f1.getName());
        }
        String passwd = readPassword();
        Credentials credentials = WalletUtil.loadCredentials(passwd, f1, hrp);

        boolean deleteResult = false;
        if (credentials.getEcKeyPair().getPrivateKey() != null) {
            System.out.println(ResourceBundleUtil.getTextString("DeleteSubmodule.text1"));
            if (StringUtil.readYesOrNo()) {
                File mnemonicFile = new File("wallet/Bip39-" + f1.getName());
                if (mnemonicFile.delete()) {
                    StringUtil.info("%s: %s", f1.getName(), ResourceBundleUtil.getTextString("DeleteSubmodule.text2"));
                } else {
                    StringUtil.warn("%s: %s", f1.getName(), ResourceBundleUtil.getTextString("DeleteSubmodule.text3"));
                    if (!StringUtil.readYesOrNo()) {
                        return Common.CANCEL_STR;
                    }
                }
                deleteResult = f1.delete();
            }
        }
        return deleteResult ? Common.SUCCESS_STR + ": " + f1.getName() + " " + ResourceBundleUtil.getTextString("deleted") :
                Common.FAIL_STR + ": " + f1.getName() + " " + ResourceBundleUtil.getTextString("deleteFail");
    }
}
