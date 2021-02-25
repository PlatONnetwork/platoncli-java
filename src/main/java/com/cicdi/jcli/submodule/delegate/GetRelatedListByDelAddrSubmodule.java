package com.cicdi.jcli.submodule.delegate;

import com.alaya.contracts.ppos.dto.CallResponse;
import com.alaya.contracts.ppos.dto.common.ErrorCode;
import com.alaya.contracts.ppos.dto.resp.DelegationIdInfo;
import com.alaya.protocol.Web3j;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.contractx.DelegateContractX;
import com.cicdi.jcli.model.NodeConfigModel;
import com.cicdi.jcli.submodule.AbstractSimpleSubmodule;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ConfigUtil;
import com.cicdi.jcli.util.JsonUtil;
import com.cicdi.jcli.util.ParamUtil;

import java.util.List;

/**
 * 查询当前账户地址所委托的节点的NodeId和质押Id
 *
 * @author haypo
 * @date 2021/1/8
 */
@Parameters(commandNames = "delegate_getRelatedListByDelAddr", commandDescription = "查询当前账户地址所委托的节点的NodeId和质押Id")
public class GetRelatedListByDelAddrSubmodule extends AbstractSimpleSubmodule {
    @Parameter(names = {"--address", "-address", "-d"}, description = "委托人账户地址", required = true)
    protected String address;

    @Override
    public String run(JCommander jc, String... argv) throws Exception {
        NodeConfigModel nodeConfigModel = ConfigUtil.readConfig(config);
        Web3j web3j = createWeb3j(nodeConfigModel);
        DelegateContractX dcx = DelegateContractX.load(web3j, nodeConfigModel.getHrp());
        String parsedAddr = ParamUtil.parseAddress(address, nodeConfigModel.getHrp());
        CallResponse<List<DelegationIdInfo>> response = dcx.getRelatedListByDelAddr(parsedAddr).send();
        return response.isStatusOk() ? JsonUtil.toPrettyJsonString(response.getData()) : Common.FAIL_STR + ":" + ErrorCode.getErrorMsg(response.getCode());
    }
}
