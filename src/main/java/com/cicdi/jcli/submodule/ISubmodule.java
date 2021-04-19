package com.cicdi.jcli.submodule;

import com.beust.jcommander.JCommander;
import com.cicdi.jcli.util.InternationalizedUsageFormatter;
import com.cicdi.jcli.util.ResourceBundleUtil;

/**
 * 子模块接口,包含account, delegate,government,hedge,node,query,staking,tx
 *
 * @author haypo
 * @date 2020/12/23
 */
public interface ISubmodule {

    /**
     * 实现cli中的各种方法
     *
     * @param jc   jc对象
     * @param argv 参数
     * @return cli方法返回结果
     * @throws Exception 一些异常
     */

    String run(JCommander jc, String... argv) throws Exception;

    /**
     * 模块中的解析
     *
     * @param jc   JCommander对象
     * @param argv 参数
     * @return 解析结果`
     * @throws Exception 解析异常
     */
    String parse(JCommander jc, String... argv) throws Exception;

    /**
     * 获取help说明
     *
     * @param jc JCommander对象
     * @return help说明
     */
    default String getHelpStr(JCommander jc) {
        JCommander thisJc = jc.getCommands().get(jc.getParsedCommand());
        StringBuilder sb = new StringBuilder();
        if (thisJc != null) {
            sb.append(ResourceBundleUtil.getTextString("cmdDescription"))
                    .append(jc.getUsageFormatter().getCommandDescription(thisJc.getProgramName())).append('\n');
            thisJc.setProgramName(jc.getProgramName() + " " + thisJc.getProgramName());
            InternationalizedUsageFormatter internationalizedUsageFormatter = new InternationalizedUsageFormatter(thisJc);
            internationalizedUsageFormatter.usage(sb);
        } else {
            jc.getUsageFormatter().usage(sb);
        }
        return sb.toString();
    }
}
