package com.cicdi.jcli.util;

import com.alaya.crypto.Address;
import com.alaya.crypto.WalletFile;
import com.alaya.utils.Files;
import com.alibaba.fastjson.JSON;
import com.cicdi.jcli.model.OldWalletFile;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author haypo
 * @date 2021/1/6
 */
public class ParamUtil {

    /**
     * 旧地址格式的正则表达式
     */
    private static final Pattern OLD_ADDRESS_PATTERN = Pattern.compile(".*address\":[\\s]*\".*");

    /**
     * 根据address参数读取地址
     *
     * @param address 可以为地址字符串，也可以为钱包json文件路径
     * @param hrp     链hrp
     * @return hrp对应的地址
     * @throws IOException 钱包json文件读取异常
     */
    public static String parseAddress(String address, String hrp) throws IOException {
        File file = new File(address);
        if (file.isFile()) {
            String fileContent = Files.readString(file);
            WalletFile walletFile;
            Matcher matcher = OLD_ADDRESS_PATTERN.matcher(fileContent);
            if (!matcher.find()) {
                walletFile = JsonUtil.readFile(file, WalletFile.class);
                Address addressObject = walletFile.getAddress();
                String mainNet = addressObject.getMainnet();
                return mainNet.startsWith(hrp) ? mainNet : addressObject.getTestnet();
            } else {
                OldWalletFile oldWalletFile = JSON.parseObject(fileContent, OldWalletFile.class);
                return ConvertUtil.formatHrpAddress(oldWalletFile.getAddress(), hrp);
            }
        } else {
            return ConvertUtil.formatHrpAddress(address, hrp);
        }
    }

    /**
     * 根据param反序列化
     *
     * @param param 可以为json文件路径，也可以为json字符串
     * @param clazz 反序列化的类
     * @param <T>   反序列化的泛型
     * @return 反序列化的对象
     */
    public static <T> T readParam(String param, Class<T> clazz) {
        File f = new File(param);
        try {
            if (f.exists() && f.isFile()) {
                return JsonUtil.readFile(f, clazz);

            } else {
                return JSON.parseObject(param, clazz);
            }
        } catch (Exception e) {
            throw new RuntimeException("Param file is not correct.");
        }
    }
}
