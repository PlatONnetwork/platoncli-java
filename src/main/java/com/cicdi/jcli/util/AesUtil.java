package com.cicdi.jcli.util;

import com.platon.utils.Numeric;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;

import static com.platon.crypto.Hash.sha256;

/**
 * aes加密类
 *
 * @author haypo
 * @date 2021/3/11
 */
public class AesUtil {
    /**
     * 算法名
     */
    public static final String KEY_ALGORITHM = "AES";

    /**
     * 加解密算法/模式/填充方式
     * 可以任意选择，为了方便后面与iOS端的加密解密，采用与其相同的模式与填充方式
     * ECB模式只用密钥即可对数据进行加密解密，CBC模式需要添加一个参数iv
     */
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";


    private final static String ivVal = "!platoncli-java!";

    private volatile static AesUtil singleton;

    private AesUtil() {
    }

    public static AesUtil getAesUtil() {
        if (singleton == null) {
            synchronized (AesUtil.class) {
                if (singleton == null) {
                    singleton = new AesUtil();
                }
            }
        }
        return singleton;
    }

    private static Key convertToKey(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
    }

    /**
     * 转化成JAVA的密钥格式
     */
    private AlgorithmParameters generateIV(String ivVal) throws NoSuchAlgorithmException, InvalidParameterSpecException {
        //iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
        byte[] iv = ivVal.getBytes();
        AlgorithmParameters params = AlgorithmParameters.getInstance(KEY_ALGORITHM);
        params.init(new IvParameterSpec(iv));
        return params;
    }

    public String encrypt(String plainText, String password) throws Exception {
        byte[] data = plainText.getBytes();
        AlgorithmParameters iv = generateIV(ivVal);
        //转化为密钥
        byte[] keyBytes = sha256(password.getBytes(StandardCharsets.UTF_8));
        Key key = convertToKey(keyBytes);
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encryptData = cipher.doFinal(data);
        return Numeric.toHexStringNoPrefix(encryptData);
    }

    /**
     * 解密
     *
     * @param encryptedStr 密文
     * @param password     密码
     * @return 明文
     */
    public String decrypt(String encryptedStr, String password) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException {
        byte[] encryptedData = Numeric.hexStringToByteArray(encryptedStr);
        byte[] keyBytes = sha256(password.getBytes(StandardCharsets.UTF_8));
        Key key = convertToKey(keyBytes);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        AlgorithmParameters iv = generateIV(ivVal);
        //设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decryptData = cipher.doFinal(encryptedData);
        return new String(decryptData);
    }


}
