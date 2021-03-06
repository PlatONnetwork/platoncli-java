package com.cicdi.jcli.util;

import com.alaya.crypto.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 钱包工具
 *
 * @author haypo
 * @date 2021/3/1
 */
@Slf4j
public class WalletUtil {
    /**
     * 创建钱包文件
     *
     * @param password       密码
     * @param file           钱包路径
     * @param useFullEncrypt 是否全加密
     * @return 是否创建成功
     */
    public static boolean createWalletFile(String password, File file, boolean useFullEncrypt) {
        try {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            generateWalletFile(password, ecKeyPair, file, useFullEncrypt);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private static void generateWalletFile(
            String password, ECKeyPair ecKeyPair, File file, boolean useFullEncrypt)
            throws CipherException, IOException {
        WalletFile walletFile;
        if (useFullEncrypt) {
            walletFile = Wallet.createStandard(password, ecKeyPair);
        } else {
            walletFile = Wallet.createLight(password, ecKeyPair);
        }
        JSON.writeJSONString(new FileWriter(file), walletFile, SerializerFeature.PrettyFormat);
    }
}
