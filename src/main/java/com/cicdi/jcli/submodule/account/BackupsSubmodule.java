package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.Bip39WalletWithPrivateKey;
import com.cicdi.jcli.model.WalletFileX;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.*;
import com.cicdi.jcli.validator.AddressValidator;
import com.platon.crypto.Bip39Wallet;
import com.platon.crypto.Credentials;
import com.platon.crypto.WalletUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * 备份钱包
 *
 * @author haypo
 * @date 2021/3/11
 */
@Slf4j
@SuppressWarnings("unused")
@Parameters(commandNames = "account_backups", resourceBundle = "command", commandDescriptionKey = "account.backups")
public class BackupsSubmodule extends AbstractSimpleSubmodule {
    private static final File BACKUP_DIR = new File("backup");
    @Parameter(names = {"--address", "-address", "-d"}, descriptionKey = "account.backups.address", required = true,
            validateWith = AddressValidator.class)
    protected String address;
    @Parameter(names = {"--type", "-type", "-t"}, descriptionKey = "account.backups.type", required = true)
    protected Type type;

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
            log.info("{}: {}", ResourceBundleUtil.getTextString("BackupsSubmodule.text1"), bip39WalletFile.getName());
            WalletFileX walletFileX = JsonUtil.readFile(bip39WalletFile, WalletFileX.class,
                    JsonUtil.readJsonSchemaFromResource("/json/WalletFileXSchema.json"));
            //解密助记词
            String mnemonic = AesUtil.getAesUtil().decrypt(walletFileX.getMnemonic(), password);
            System.out.println("Address: " + credentials.getAddress() + " Mnemonic: " + mnemonic);
            //创建助记词备份文件
            Bip39Wallet bip39Wallet = new Bip39Wallet(filename, mnemonic);
            String backupFileName = credentials.getAddress() + ".mnemonic.backup";

            String finalFilename = JsonUtil.writeJsonFileWithNoConflict(BACKUP_DIR, backupFileName, bip39Wallet);
            return Common.SUCCESS_STR + " " + ResourceBundleUtil.getTextString("BackupsSubmodule.text2") + ":" + finalFilename;
        }
        throw new RuntimeException(ResourceBundleUtil.getTextString("BackupsSubmodule.text3") + ": " + filename);
    }

    /**
     * 备份私钥
     *
     * @param credentials 已经解锁的钱包
     * @return 备份结果
     * @throws IOException io异常
     */
    public static String backupPrivateKey(Credentials credentials, String filename) throws IOException {
        String privateKeyHex = credentials.getEcKeyPair().getPrivateKey().toString(16);
        System.out.println(ResourceBundleUtil.getTextString("address") + ": "
                + credentials.getAddress() + " " + ResourceBundleUtil.getTextString("privatKey") + ": " + privateKeyHex);
        Bip39WalletWithPrivateKey bip39WalletWithPrivateKey = new Bip39WalletWithPrivateKey(filename, privateKeyHex);
        String backupFileName = credentials.getAddress() + ".privateKey.backup";

        String finalFilename = JsonUtil.writeJsonFileWithNoConflict(BACKUP_DIR, backupFileName, bip39WalletWithPrivateKey);
        return Common.SUCCESS_STR + " " + ResourceBundleUtil.getTextString("BackupsSubmodule.text4") + ": " + finalFilename;
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
            log.info("{}:{}", ResourceBundleUtil.getTextString("foundWalletFile"), filename);
            password = StringUtil.readPassword();
            credentials = WalletUtils.loadCredentials(password, filename);
            return type.process(password, credentials, filename);
        }
        return type.process(password, credentials, filename);
    }
}
