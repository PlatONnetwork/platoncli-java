package com.cicdi.jcli.util;

import com.cicdi.jcli.contractx.DelegateContractX;
import com.cicdi.jcli.contractx.NodeContractX;
import com.platon.contracts.ppos.dto.resp.DelegationIdInfo;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.protocol.Web3j;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haypo
 * @date 2021/3/23
 */
public class VerifyUtil {
    /**
     * @param web3j web3对象
     * @param hrp   hrp值
     * @return 节点列表
     * @throws Exception 交易异常
     */
    private static List<Node> getNodeList(Web3j web3j, String hrp) throws Exception {
        return NodeContractX.load(web3j, hrp).getCandidateList().send().getData();
    }

    /**
     * 校验stakingBlockNum
     *
     * @param web3j           web3
     * @param hrp             hrp
     * @param nodeId          节点id
     * @param stakingBlockNum 质押块高
     * @throws Exception 获取节点列表异常
     */
    public static void verifyStakingBlockNum(Web3j web3j, String hrp, String nodeId, BigInteger stakingBlockNum) throws Exception {
        List<Node> nodeList = getNodeList(web3j, hrp)
                .stream().filter(s -> s.getNodeId().equals(nodeId) || s.getNodeId().equals(nodeId.substring(2)))
                .collect(Collectors.toList());
        if (nodeList.size() != 1) {
            throw new RuntimeException("NodeId is invalid");
        }
        if (!nodeList.get(0).getStakingBlockNum().equals(stakingBlockNum)) {
            throw new RuntimeException("StakingBlockNum is invalid");
        }
    }

    /**
     * 校验是否已委托的节点id和块高
     */
    public static void verifyDelNodeIdAndStakingBlockNum(Web3j web3j, String hrp, String nodeId, String address, BigInteger stakingBlockNum) throws Exception {
        DelegateContractX dcx = DelegateContractX.load(web3j, hrp);
        List<DelegationIdInfo> delegationIdInfoList = dcx.getRelatedListByDelAddr(address).send().getData();
        for (DelegationIdInfo delegationIdInfo : delegationIdInfoList) {
            if (delegationIdInfo.getNodeId().equals(nodeId) ||
                    delegationIdInfo.getNodeId().equals(nodeId.substring(2))) {
                if (delegationIdInfo.getStakingBlockNum().equals(stakingBlockNum)) {
                    return;
                } else {
                    throw new RuntimeException("StakingBlockNum is invalid");
                }
            }
        }
        throw new RuntimeException("NodeId is un-delegated");
    }

    public static void verifyNodeId(Web3j web3j, String hrp, String nodeId) throws Exception {
        List<String> nodeIdList = getNodeList(web3j, hrp).stream().map(Node::getNodeId).collect(Collectors.toList());
        if (!nodeIdList.contains(nodeId) && !nodeIdList.contains(nodeId.substring(2))) {
            throw new RuntimeException("NodeId is invalid");
        }
    }
}
