package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.JsonUtil;
import com.cicdi.jcli.util.StringUtil;
import com.cicdi.jcli.util.WalletUtil;
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
@Parameters(commandNames = "account_recovery", commandDescription = "恢复私钥")
public class RecoverySubmodule extends AbstractSimpleSubmodule {
    private static final File RECOVERY_DIR = new File("recovery");
    @Parameter(names = {"--type", "-type", "-t"}, description = "选择恢复方式，助记词或者私钥。--type mnemonic 或者--type privateKey", required = true)
    protected String type;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        String password;
        WalletFile walletFile;
        String recoveryFilename;
        switch (type) {
            case "m":
            case "mnemonic":
                String mnemonic = StringUtil.readMnemonic();
                password = StringUtil.readPasswordTwice();
                walletFile = WalletUtil.genWalletByMnemonic(mnemonic, password);
                recoveryFilename = walletFile.getAddress() + ".mnemonic.recovery";
                recoveryFilename = JsonUtil.writeJsonFileWithNoConflict(RECOVERY_DIR, recoveryFilename, walletFile);
                break;
            case "p":
            case "privateKey":
                String pk = StringUtil.readPrivateKey();
                password = StringUtil.readPasswordTwice();
                walletFile = WalletUtil.genWalletByPrivateKey(pk, password);
                recoveryFilename = walletFile.getAddress() + ".privateKey.recovery";
                recoveryFilename = JsonUtil.writeJsonFileWithNoConflict(RECOVERY_DIR, recoveryFilename, walletFile);
                break;
            default:
                throw new RuntimeException("不支持的type参数");
        }
        return Common.SUCCESS_STR + " 成功恢复钱包文件：" + recoveryFilename;
    }
}
