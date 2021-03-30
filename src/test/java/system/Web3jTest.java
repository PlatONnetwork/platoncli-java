package system;

import com.cicdi.jcli.contractx.NodeContractX;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ConvertUtil;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.protocol.Web3j;
import com.platon.protocol.Web3jService;
import com.platon.protocol.http.HttpService;
import org.junit.Test;

import java.math.BigInteger;


/**
 * @author haypo
 * @date 2021/1/7
 */
public class Web3jTest {


    @Test
    public void testWeb3j() throws Exception {
        Web3jService web3jService = new HttpService(Common.DEFAULT_NODE_URL);
        Web3j web3j = Web3j.build(web3jService);
        BigInteger sumShare = BigInteger.ZERO;
        for (Node node : NodeContractX.load(web3j, "atp").getCandidateList().send().getData()) {
            sumShare = sumShare.add(node.getShares());
            System.out.println(
                    "0x" + node.getNodeId()
                            + "\t" + node.getNodeName()
                            + "\t" + ConvertUtil.von2Hrp(node.getShares())
//                    + "\t" + node.getStakingEpoch()
//                    + "\t" + node.getDelegateEpoch()
            );
        }
//        PlatonEvidences evidences = web3j.platonEvidences().send();
//        System.out.println(JsonUtil.toPrettyJsonString(evidences.getEvidences()));
//        PlatonNodeInfo response = Web3jUtil.getNodeInfo(new HttpService("http://192.168.31.97:6257")).send();
//        Peers peers = Web3jUtil.getPeers(new HttpService("http://192.168.31.97:6257")).send();
//        System.out.println(response.getNodeInfo().getBlsPubKey());
//        if (response.getError() != null) {
//            throw new Error(response.getError().getMessage());
//        }
    }
}
