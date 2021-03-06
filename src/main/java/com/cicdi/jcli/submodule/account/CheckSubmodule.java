package com.cicdi.jcli.submodule.account;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.model.Tuple;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.AddressUtil;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 查看本地钱包
 *
 * @author haypo
 * @date 2021/3/1
 */
@Slf4j
@SuppressWarnings("unused")
@Parameters(commandNames = "account_check", commandDescription = "查看本地钱包")
public class CheckSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--address", "-address", "-d"}, description = "具体查看的钱包文件名称或者address，不填写具体参数查询默认目录下全部钱包文件")
    protected String address;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtil.isBlank(address)) {
            List<Tuple<String, String>> tuples = AddressUtil.readAddressFromDir(nodeConfigModel.getHrp());
            for (Tuple<String, String> tuple : tuples) {
                stringBuilder.append("File name: ").append(tuple.getB()).append(", address: ").append(tuple.getA()).append(".\n");
            }
        } else {
            String fileName = AddressUtil.getFileNameFromAddress(nodeConfigModel.getHrp(), address);
            stringBuilder.append("File name: ").append(fileName).append(", address: ").append(address).append(".\n");
        }
        return stringBuilder.toString();
    }
}
