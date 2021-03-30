package com.cicdi.jcli.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 备份私钥时的文件
 * @author haypo
 * @date 2021/3/12
 */
@Data
@AllArgsConstructor
public class Bip39WalletWithPrivateKey {
    private final String filename;
    private final String privateKey;
}
