package com.cicdi.jcli.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * git工具
 *
 * @author haypo
 * @date 2021/2/19
 */
public class GitUtil {

    /**
     * 获得git的提交id
     *
     * @return git的提交id
     */
    public static String getCommitId() {
        try {
            Process process = Runtime.getRuntime().exec("git rev-parse HEAD");
            InputStream in = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString();
        } catch (Exception e) {
            return "Can not get revision.";
        }
    }
}
