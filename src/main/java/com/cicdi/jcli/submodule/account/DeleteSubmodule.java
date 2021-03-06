package com.cicdi.jcli.submodule.account;

import com.alaya.crypto.Credentials;
import com.alaya.crypto.WalletUtils;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.Common;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Scanner;

import static com.cicdi.jcli.util.StringUtil.readPassword;

/**
 * 删除钱包
 *
 * @author haypo
 * @date 2021/3/1
 */
@Slf4j
@SuppressWarnings("unused")
@Parameters(commandNames = "account_delete", commandDescription = "删除钱包")
public class DeleteSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--address", "-address", "-d"}, required = true, description = "指定钱包文件或者钱包文件地址删除对应钱包文件")
    protected String address;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        File f1 = new File(address);
        boolean deleteResult = false;
        if (f1.exists() && f1.isFile()) {
            String passwd = readPassword();
            Credentials credentials = WalletUtils.loadCredentials(passwd, f1);
            if (credentials.getAddress() != null) {
                System.out.println("Do you want to delete wallet file: " + f1.getName() + "? Y/N");
                String s = new Scanner(System.in).nextLine();
                if (Common.LETTER_Y.equalsIgnoreCase(s)) {
                    deleteResult = f1.delete();
                }
            }
        }
        return deleteResult ? Common.SUCCESS_STR : Common.FAIL_STR;
    }
}
