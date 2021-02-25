package com.cicdi.jcli;

import com.alaya.crypto.CipherException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.MissingCommandException;
import com.beust.jcommander.Parameter;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.submodule.ISubmodule;
import com.cicdi.jcli.submodule.SubModuleScanner;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.GitUtil;
import com.cicdi.jcli.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * @author haypo
 * @date 2020/12/22
 */
@Slf4j
public class Main extends AbstractSimpleSubmodule {
    private static final String CLI_VERSION = "0.1.0";
    private static final String CLI_NAME = "platoncli-java";
    private static final String PLATON_NAME = "platon";
    public static JCommander jc = null;
    public static Main main = new Main();
    public static String result;
    static Map<String, ISubmodule> iSubModuleMap = SubModuleScanner.scanSubModule();
    @Parameter(names = {"--version", "-version", "-v"}, description = "platon-jcli 版本号")
    protected boolean version;

    private static JCommander parseArgs(String... argv) {
        jc = JCommander.newBuilder()
                .addObject(main)
                .build();
        jc.setProgramName(Common.PROGRAM_NAME);
        for (ISubmodule subModule : iSubModuleMap.values()) {
            jc.addCommand(subModule);
        }
        jc.parse(argv);
        return jc;
    }

    public static void main(String... argv) {
        try {
            jc = parseArgs(argv);
            result = main.parse(jc, argv);
            StringBuilder argStr = new StringBuilder();
            for (String s : argv) {
                argStr.append(" ").append(s);
            }
            log.info("\r\nCommand: java -jar platoncli-java-jar-with-dependencies.jar{}\r\nResult: {}", argStr.toString(), result);
        } catch (Exception exception) {
            if (exception instanceof MissingCommandException) {
                log.error("command not found", exception);
                if (jc != null) {
                    jc.usage();
                }
                return;
            }
            if (exception instanceof CipherException) {
                log.error(exception.getMessage(), exception);
                return;
            }
            log.error(exception.getMessage(), exception);

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