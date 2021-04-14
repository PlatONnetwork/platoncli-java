package com.cicdi.jcli.submodule.hedge;

import com.platon.contracts.ppos.dto.resp.RestrictingItem;
import com.platon.protocol.Web3j;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.RestrictPlanContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.AddressUtil;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取锁仓计划
 *
 * @author haypo
 * @date 2021/3/15
 */
@Slf4j
@SuppressWarnings("unused")
@Parameters(commandNames = "hedge_getRestrictingInfo", resourceBundle = "command", commandDescriptionKey = "hedge.getRestrictingInfo")
public class GetRestrictingInfoSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--address", "-address", "-d"}, descriptionKey = "address", required = true)
    protected String address;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        Web3j web3j = createWeb3j();
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        address = AddressUtil.readAddress(address, nodeConfigModel.getHrp());
        RestrictingItem restrictingItem = RestrictPlanContractX.load(web3j, nodeConfigModel.getHrp()).getRestrictingInfo(address).send().getData();
        return JsonUtil.toPrettyJsonString(restrictingItem);
    }
}
