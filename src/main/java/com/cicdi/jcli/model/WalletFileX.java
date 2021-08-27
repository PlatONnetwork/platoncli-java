package com.cicdi.jcli.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 该对象实例化存储在wallet文件夹下，用于备份私钥和助记词
 *
 * @author haypo
 * @date 2021/3/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletFileX {
    private String address;
    private String filename;
    private String mnemonic;
    private String hrp;
}
