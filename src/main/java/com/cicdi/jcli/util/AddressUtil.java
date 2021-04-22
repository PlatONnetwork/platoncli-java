package com.cicdi.jcli.util;

import com.alibaba.fastjson.JSON;
import com.cicdi.jcli.model.Tuple;
import com.platon.bech32.Bech32;
import com.platon.crypto.WalletFile;
import com.platon.utils.Files;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 处理地址的工具类
 *
 * @author haypo
 * @date 2021/3/1
 */
@Slf4j
public class AddressUtil {
    /**
     * 格式化地址，例如将lax开头的地址转换为atp开头的，方便对多种钱包文件实现兼容
     *
     * @param address 原始地址
     * @param hrp     地址前缀
     * @return 符合地址前缀的格式化的地址
     */
    public static String formatHrpAddress(String address, String hrp) {
        if (address.startsWith(hrp)) {
            log.info(ResourceBundleUtil.getTextString("addressInWalletFile") + ": {} " +
                    ResourceBundleUtil.getTextString("matches") + "hrp: {}", address, hrp);
            return address;
        }
        log.warn(ResourceBundleUtil.getTextString("addressInWalletFile") + ": {} " +
                ResourceBundleUtil.getTextString("notMatches") + "hrp: {} " + ResourceBundleUtil.getTextString("autoConvert"), address, hrp);
        try {
            String hexAddress = Bech32.addressDecodeHex(address);
            return Bech32.addressEncode(hrp, hexAddress);
        } catch (Exception e) {
            return Bech32.addressEncode(hrp, address);
        }
    }

    /**
     * 在jar所在目录查找匹配地址的钱包文件
     *
     * @param hrp     hrp值
     * @param address 地址
     * @return 钱包文件
     * @throws FileNotFoundException 未找到钱包文件
     */
    public static File getFileFromAddress(String hrp, String address) throws FileNotFoundException {
        if (new File(address).isFile()) {
            return new File(address);
        }
        return new File(getFilenameFromAddress(hrp, address));
    }

    /**
     * 根据地址查询钱包名称
     *
     * @param hrp     hrp值，用于格式化地址
     * @param address 查询地址
     * @return 钱包文件名称
     * @throws FileNotFoundException 无法查找到钱包
     */
    public static String getFilenameFromAddress(String hrp, String address) throws FileNotFoundException {
        List<Tuple<String, String>> tuples = readAddressFromDir(hrp);
        for (Tuple<String, String> t : tuples) {
            if (t.getA().equals(address)) {
                return t.getB();
            }
        }
        throw new FileNotFoundException("can not find wallet file matches address: " + address);
    }

    /**
     * Tuple<String, String> address,filename
     * 从jar所在文件夹下读取钱包地址和钱包名称
     *
     * @param hrp hrp值
     * @return 钱包地址, 钱包名称的元组列表
     */
    public static List<Tuple<String, String>> readAddressFromDir(String hrp) {
        File root = new File("./");
        List<Tuple<String, String>> addressList = new ArrayList<>();
        File[] files = root.listFiles(file -> file.isFile() && file.getName().contains("json") ||
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

    /**
     * 根据address读取地址
     *
     * @param address 地址或钱包json
     * @param hrp     hrp值
     * @return 地址
     * @throws IOException io异常
     */
    public static String readAddress(String address, String hrp) throws IOException {
        //判断address是否是地址还是json
        if (JsonUtil.isJsonFile(address)
        ) {
            File file = new File(address);
            if (!file.isFile() || !file.exists()) {
                throw new FileNotFoundException(file.getName() + " not exist");
            }
            return readAddressFromFile(new File(address), hrp);
        }
        return formatHrpAddress(address, hrp);
    }

    public static String readAddressFromFile(File file, String hrp) throws IOException {
        String fileContent = Files.readString(file);
        fileContent = fileContent.replaceAll(WalletUtil.MAIN_TEST_ADDRESS_REGEX, "\"address\": \"$1\"");
        WalletFile walletFile = JSON.parseObject(fileContent, WalletFile.class);
        return formatHrpAddress(walletFile.getAddress(), hrp);
    }
}
