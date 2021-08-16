package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.*;
import com.platon.crypto.WalletFile;

import java.io.File;

/**
 * 恢复私钥
 *
 * @author haypo
 * @date 2021/3/13
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "account_recovery", resourceBundle = "command", commandDescriptionKey = "account.recovery")
public class RecoverySubmodule extends AbstractSimpleSubmodule {
    private static final File RECOVERY_DIR = new File("recovery");
    @Parameter(names = {"--type", "-type", "-t"}, descriptionKey = "account.recovery.type", required = true)
    protected Type type;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        type.process();
        return Common.SUCCESS_STR;
    }

    enum Type {
        /**
         * m, mnemonic: 助记词
         * pk, p, privateKey: 私钥
         */
        m, mnemonic, pk, p, privateKey;

        public void process()
                throws Exception {
            switch (this) {
                case m:
                case mnemonic: {
                    String mnemonic = StringUtil.readMnemonic();
                    String password = StringUtil.readPasswordTwice();
                    WalletFile walletFile = WalletUtil.genWalletByMnemonic(mnemonic, password);
                    StringUtil.info("%s: %s", ResourceBundleUtil.getTextString("RecoverySubmodule.text1"), JsonUtil.toPrettyJsonString(walletFile));
                    break;
                }
                default: {
                    String pk = StringUtil.readPrivateKey();
                    String password = StringUtil.readPasswordTwice();
                    WalletFile walletFile = WalletUtil.genWalletByPrivateKey(pk, password);
                    StringUtil.info("%s: %s", ResourceBundleUtil.getTextString("RecoverySubmodule.text1"), JsonUtil.toPrettyJsonString(walletFile));
                    break;
                }
            }
        }
    }
}
