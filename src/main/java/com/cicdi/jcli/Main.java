package com.cicdi.jcli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.MissingCommandException;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.submodule.ISubmodule;
import com.cicdi.jcli.submodule.SubModuleScanner;
import com.cicdi.jcli.util.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * @author haypo
 * @date 2020/12/22
 */
@Parameters(resourceBundle = "command")
public class Main extends AbstractSimpleSubmodule {
    private static final String CLI_VERSION = "0.1.0";
    private static final String CLI_NAME = "platoncli-java";
    private static final String PLATON_NAME = "platon";
    public static JCommander jc = null;
    public static Main main = new Main();
    public static String result;
    static Map<String, ISubmodule> iSubModuleMap = new HashMap<>(50);
    @Parameter(names = {"--version", "-version", "-v"}, description = "platon-jcli 版本号")
    protected boolean version;

    private static JCommander parseArgs(String... argv) {
        jc = JCommander.newBuilder()
                .addObject(main)
                .build();
        jc.setProgramName(Common.PROGRAM_NAME);
        iSubModuleMap = SubModuleScanner.scanSubModule();
        for (ISubmodule subModule : iSubModuleMap.values()) {
            jc.addCommand(subModule);
        }
        jc.parse(argv);
        return jc;
    }

    public static void main(String... argv) {
        try {
            StringBuilder argStr = new StringBuilder();
            for (String s : argv) {
                argStr.append(" ").append(s);
            }
            String jarName = Main.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            jarName = new File(jarName).getName();
            StringUtil.info("%s: java -jar %s%s", ResourceBundleUtil.getTextString("command"), jarName, argStr);
            jc = parseArgs(argv);
            result = main.parse(jc, argv);
            StringUtil.info("%s: %s", ResourceBundleUtil.getTextString("result"), result);
        } catch (Exception exception) {
            exception.printStackTrace();
            if (exception instanceof MissingCommandException) {
                if (jc != null) {
                    jc.usage();
                }
            }
        }
    }

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        if (version) {
            return "version: " + CLI_VERSION + "\r\nrevision: " + GitUtil.getCommitId() + "\r\ntimestamp: " + TimeUtil.getTimestamp();
        }
        if (iSubModuleMap.containsKey(jc.getParsedCommand())) {
            return iSubModuleMap.get(jc.getParsedCommand()).parse(jc, argv);
        } else {
            return super.getHelpStr(jc);
        }
    }

}