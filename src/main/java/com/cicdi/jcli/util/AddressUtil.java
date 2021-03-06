package com.cicdi.jcli.util;

import com.alaya.bech32.Bech32;
import com.alaya.crypto.Address;
import com.alaya.crypto.WalletFile;
import com.alaya.utils.Files;
import com.alaya.utils.JSONUtil;
import com.cicdi.jcli.model.OldWalletFile;
import com.cicdi.jcli.model.Tuple;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author haypo
 * @date 2021/3/1
 */
@Slf4j
public class AddressUtil {
    /**
     * 旧地址格式的正则表达式
     */
    private static final Pattern OLD_ADDRESS_PATTERN = Pattern.compile(".*address\":[\\s]*\".*");

    /**
     * 格式化地址，例如将lax开头的地址转换为atp开头的，方便对多种钱包文件实现兼容
     *
     * @param address 原始地址
     * @param hrp     地址前缀
     * @return 符合地址前缀的格式化的地址
     */
    public static String formatHrpAddress(String address, String hrp) {
        if (address.startsWith(hrp)) {
            return address;
        }
        try {
            String hexAddress = Bech32.addressDecodeHex(address);
            return Bech32.addressEncode(hrp, hexAddress);
        } catch (Exception e) {
            return Bech32.addressEncode(hrp, address);
        }
    }

    public static String getFileNameFromAddress(String hrp, String address) throws FileNotFoundException {
        List<Tuple<String, String>> tuples = readAddressFromDir(hrp);
        for (Tuple<String, String> t : tuples) {
            if (t.getA().equals(address)) {
                return t.getB();
            }
        }
        throw new FileNotFoundException("can not find wallet file matches address: " + address);
    }

    public static List<Tuple<String, File>> readAddressFileFromDir(String hrp, String dir) {
        File root = new File(dir);
        List<Tuple<String, File>> addressFileTuple = new ArrayList<>();
        File[] files = root.listFiles(file -> file.getName().contains("json") ||
                file.getName().contains("JSON"));
        if (files != null) {
            for (File file : files) {
                try {
                    addressFileTuple.add(Tuple.create(
                            readAddressFromFile(file, hrp),
                            file));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return addressFileTuple;
    }

    public static List<Tuple<String, String>> readAddressFromDir(String hrp) {
        File root = new File("./");
        List<Tuple<String, String>> addressList = new ArrayList<>();
        File[] files = root.listFiles(file -> file.getName().contains("json") ||
                file.getName().contains("JSON"));
        if (files != null) {
            for (File file : files) {
                try {
                    addressList.add(Tuple.create(
                            readAddressFromFile(file, hrp),
                            file.getName()));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return addressList;
    }

    public static String readAddressFromFile(File file, String hrp) throws IOException {
        String fileContent = Files.readString(file);
        WalletFile walletFile;
        Matcher matcher = OLD_ADDRESS_PATTERN.matcher(fileContent);
        if (!matcher.find()) {
            walletFile = JsonUtil.readFile(file, WalletFile.class);
            Address addressObject = walletFile.getAddress();
            String mainNet = addressObject.getMainnet();
            if (mainNet.startsWith(hrp)) {
                return mainNet;
            }
            if (addressObject.getTestnet().startsWith(hrp)) {
                return fileContent;
            }
            throw new RuntimeException("wallet file not match hrp");
        } else {
            OldWalletFile oldWalletFile = JSONUtil.parseObject(fileContent, OldWalletFile.class);
            return AddressUtil.formatHrpAddress(oldWalletFile.getAddress(), hrp);
        }
    }
}
