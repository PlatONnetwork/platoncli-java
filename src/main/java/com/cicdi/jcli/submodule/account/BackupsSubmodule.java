package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.WalletFileX;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.*;
import com.cicdi.jcli.validator.AddressValidator;
import com.platon.crypto.Credentials;
import com.platon.crypto.WalletUtils;

import java.io.File;

/**
 * 备份钱包
 *
 * @author haypo
 * @date 2021/3/11
 */
@SuppressWarnings("unused")
@Parameters(commandNames = "account_backups", resourceBundle = "command", commandDescriptionKey = "account.backups")
public class BackupsSubmodule extends AbstractSimpleSubmodule {
    private static final File BACKUP_DIR = new File("backup");
    @Parameter(names = {"--address", "-address", "-d"}, descriptionKey = "account.backups.address", required = true,
            validateWith = AddressValidator.class)
    protected String address;
    @Parameter(names = {"--type", "-type", "-t"}, descriptionKey = "account.backups.type", required = true)
    protected Type type;

    /**
     * 备份助记词
     *
     * @param password    密码
     * @param credentials 钱包
     * @param filename    钱包文件名
     * @return 备份结果
     */
    public static String backupMnemonic(String password, Credentials credentials, String filename) throws Exception {
        String bip39WalletName = "Bip39-" + filename;
        File bip39WalletFile = new File("wallet/" + bip39WalletName);
        if (bip39WalletFile.isFile()) {
            StringUtil.info("%s: %s", ResourceBundleUtil.getTextString("BackupsSubmodule.text1"), bip39WalletFile.getName());
            WalletFileX walletFileX = JsonUtil.readFile(bip39WalletFile, WalletFileX.class,
                    JsonUtil.readJsonSchemaFromResource("/json/WalletFileXSchema.json"));
            //解密助记词
            if (walletFileX.getAddress().equals(credentials.getAddress())) {
                String mnemonic = AesUtil.getAesUtil().decrypt(walletFileX.getMnemonic(), password);
                return ResourceBundleUtil.getTextString("address") + ": " + credentials.getAddress() + " " + ResourceBundleUtil.getTextString("mnemonic") + ": " + mnemonic;
            } else {
                throw new RuntimeException("Address mismatch");
            }
        }
        throw new RuntimeException(ResourceBundleUtil.getTextString("BackupsSubmodule.text3") + ": " + filename);
    }

    /**
     * 备份私钥
     *
     * @param credentials 已经解锁的钱包
     * @return 备份结果
     */
    public static String backupPrivateKey(Credentials credentials, String filename) {
        String privateKeyHex = credentials.getEcKeyPair().getPrivateKey().toString(16);
        return ResourceBundleUtil.getTextString("address") + ": "
                + credentials.getAddress() + " " + ResourceBundleUtil.getTextString("privatKey") + ": " + privateKeyHex;
    }

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        File file = new File(address);
        String password, filename;
        Credentials credentials;
        if (file.isFile()) {
            //如果是钱包json文件
            filename = file.getName();
            password = StringUtil.readPassword();
            credentials = WalletUtils.loadCredentials(password, file);
        } else {
            //如果是钱包地址
            filename = AddressUtil.getFilenameFromAddress(ConfigUtil.readConfig(config).getHrp(), address);
            System.out.printf("%s: %s", ResourceBundleUtil.getTextString("foundWalletFile"), filename);
            password = StringUtil.readPassword();
            credentials = WalletUtils.loadCredentials(password, filename);
            return type.process(password, credentials, filename);
        }
        return type.process(password, credentials, filename);
    }

    enum Type {
        /**
         *
         */
        m, mnemonic, pk, p, privateKey;

        public String process(String password, Credentials credentials, String filename)
                throws Exception {
            switch (this) {
                case m:
                case mnemonic:
                    return backupMnemonic(password, credentials, filename);
                case p:
                case pk:
                case privateKey:
                    return backupPrivateKey(credentials, filename);
                default:
                    throw new IllegalArgumentException("type");
            }
        }
    }
}
