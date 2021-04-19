package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.JsonUtil;
import com.cicdi.jcli.util.ResourceBundleUtil;
import com.cicdi.jcli.util.StringUtil;
import com.cicdi.jcli.util.WalletUtil;
import com.cicdi.jcli.validator.AccountOperationTypeValidator;
import com.platon.crypto.WalletFile;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 恢复私钥
 *
 * @author haypo
 * @date 2021/3/13
 */
@Slf4j
@SuppressWarnings("unused")
@Parameters(commandNames = "account_recovery", resourceBundle = "command", commandDescriptionKey = "account.recovery")
public class RecoverySubmodule extends AbstractSimpleSubmodule {
    private static final File RECOVERY_DIR = new File("recovery");
    @Parameter(names = {"--type", "-type", "-t"}, descriptionKey = "account.recovery.type", required = true)
    protected Type type;

    enum Type {
        /**
         *
         */
        m, mnemonic, pk, p, privateKey;

        public String process()
                throws Exception {
            switch (this) {
                case m:
                case mnemonic:
                    String mnemonic = StringUtil.readMnemonic();
                    String password = StringUtil.readPasswordTwice();
                    WalletFile walletFile = WalletUtil.genWalletByMnemonic(mnemonic, password);
                    String recoveryFilename = walletFile.getAddress() + ".mnemonic.recovery";
                    recoveryFilename = JsonUtil.writeJsonFileWithNoConflict(RECOVERY_DIR, recoveryFilename, walletFile);
                    return recoveryFilename;
                default:
                    String pk = StringUtil.readPrivateKey();
                    password = StringUtil.readPasswordTwice();
                    walletFile = WalletUtil.genWalletByPrivateKey(pk, password);
                    recoveryFilename = walletFile.getAddress() + ".privateKey.recovery";
                    recoveryFilename = JsonUtil.writeJsonFileWithNoConflict(RECOVERY_DIR, recoveryFilename, walletFile);
                    return recoveryFilename;
            }
        }
    }

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        String recoveryFilename = type.process();
        return ResourceBundleUtil.getTextString("RecoverySubmodule.text1") + ": " + recoveryFilename;
    }
}
