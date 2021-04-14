package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.Bip39WalletWithPrivateKey;
import com.cicdi.jcli.model.WalletFileX;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.*;
import com.platon.crypto.Bip39Wallet;
import com.platon.crypto.Credentials;
import com.platon.crypto.WalletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

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
    @Parameter(names = {"--address", "-address", "-d"}, descriptionKey = "account.backups.address", required = true)
    protected String address;
    @Parameter(names = {"--type", "-type", "-t"}, descriptionKey = "account.backups.type", required = true)
    protected String type;

    /**
     * 备份助记词
     *
     * @param password    密码
     * @param credentials 钱包
     * @param filename    钱包文件名
     * @return 备份结果
     * @throws java.io.IOException                io异常
     * @throws InvalidAlgorithmParameterException 算法参数异常
     * @throws InvalidKeyException                无效密码
     * @throws BadPaddingException                解密错误
     * @throws IllegalBlockSizeException          块非法错误
     * @throws NoSuchPaddingException             无法填补
     * @throws NoSuchAlgorithmException           无此算法
     * @throws InvalidParameterSpecException      无效参数
     */
    public static String backupMnemonic(String password, Credentials credentials, String filename) throws java.io.IOException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException {
        String bip39WalletName = "Bip39-" + filename;
        File bip39WalletFile = new File("wallet/" + bip39WalletName);
        if (bip39WalletFile.isFile()) {
            log.info("{}: {}", ResourceBundleUtil.getTextString("BackupsSubmodule.text1"), bip39WalletFile.getName());
            WalletFileX walletFileX = JsonUtil.readFile(bip39WalletFile, WalletFileX.class, null);
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
        if (file.isFile()) {
            //如果是钱包json文件
            String filename = file.getName();
            String password = StringUtil.readPassword();
            Credentials credentials = WalletUtils.loadCredentials(password, file);
            switch (type) {
                case "m":
                case "mnemonic":
                    return backupMnemonic(password, credentials, file.getName());
                case "p":
                case "pk":
                case "privateKey":
                    return backupPrivateKey(credentials, filename);
                default:
                    throw new IllegalArgumentException("type");
            }
        } else {
            //如果是钱包地址
            String filename = AddressUtil.getFilenameFromAddress(ConfigUtil.readConfig(config).getHrp(), address);
            log.info("{}:{}", ResourceBundleUtil.getTextString("foundWalletFile"), filename);
            String password = StringUtil.readPassword();
            Credentials credentials = WalletUtils.loadCredentials(password, filename);
            switch (type) {
                case "m":
                case "mnemonic":
                    return backupMnemonic(password, credentials, filename);
                case "p":
                case "pk":
                case "privateKey":
                    return backupPrivateKey(credentials, filename);
                default:
                    throw new IllegalArgumentException("type");
            }
        }
    }
}
