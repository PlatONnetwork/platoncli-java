package com.cicdi.jcli.model;

import com.alaya.crypto.WalletFile;
import lombok.Data;

/**
 * @author haypo
 * @date 2021/1/11
 */
@Data
public class OldWalletFile {
    private String address;
    private WalletFile.Crypto crypto;
    private String id;
    private int version;

}
