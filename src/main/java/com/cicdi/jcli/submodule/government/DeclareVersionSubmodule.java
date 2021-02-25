package com.cicdi.jcli.submodule.government;

import com.alaya.crypto.CipherException;
import com.beust.jcommander.Parameters;
import com.cicdi.jcli.submodule.AbstractComplexSubmodule;
import com.cicdi.jcli.submodule.FunctionUtil;
import com.cicdi.jcli.template.government.DeclareVersionTemplate;
import com.cicdi.jcli.util.contract.government.DeclareVersionUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 版本声明
 *
 * @author haypo
 * @date 2020/12/31
 */
@Slf4j
@Parameters(commandNames = "government_declareVersion", commandDescription = "版本声明")
public class DeclareVersionSubmodule extends AbstractComplexSubmodule<DeclareVersionTemplate, DeclareVersionUtil> {
    @Override
    public FunctionUtil<DeclareVersionUtil> function() throws IOException, CipherException {
        return () -> new DeclareVersionUtil(isOnline(), address, config, param, DeclareVersionTemplate.class);
    }

    @Override
    public String generateTemplate() {
        return "类型        必填性         参数名称          参数解释\n" +
                "String     must          activeNode       声明的节点id，只能是验证人/候选人。若为空，则默认使用config配置的节点的id\n";
    }

//    @Override
//    public String run(JCommander jc, String... argv) throws Exception {
//        NodeConfigModel nodeConfigModel = readNodeConfig();
//        Web3j web3j = createWeb3j(nodeConfigModel);
//        String nodeId = Web3jUtil.getNodeInfo(nodeConfigModel.getRpcAddress()).send().getNodeInfo().getId();
//
//        if (isOnline()) {
//            String password = StringUtil.readPassword();
//            Credentials credentials = WalletUtils.loadCredentials(password, address);
//            ProposalContractX pcx = ProposalContractX.load(web3j, credentials, nodeConfigModel.getChainId(), nodeConfigModel.getHrp());
//
//            RemoteCall<TransactionResponse> remoteCall = pcx.declareVersion(
//                    web3j.getProgramVersion().send().getAdminProgramVersion(),
//                    nodeId,
//                    Common.genGasProvider(gasPrice, gasLimit)
//            );
//            TransactionResponse response = remoteCall.send();
//            log.info(response.toString());
//            return response.isStatusOk() ? Common.SUCCESS_STR : Common.FAIL_STR;
//        } else {
//            AdminProgramVersion adminProgramVersion = web3j.getProgramVersion().send();
//            Function function = ProposalContractX.createDeclareVersionFunction(adminProgramVersion.getAdminProgramVersion(), nodeId);
//            BaseTemplate4Serialize baseTemplate4Serialize = convert2BaseTemplate4Serialize(function, nodeConfigModel, web3j, address, gasLimit, gasPrice, fast);
//            return QrUtil.save2QrCodeImage(getQrCodeImagePrefix(), baseTemplate4Serialize);
//        }
//    }
}
