package com.cicdi.jcli.util;

import com.alaya.protocol.Web3jService;
import com.alaya.protocol.core.Request;
import com.cicdi.jcli.model.Peers;
import com.cicdi.jcli.model.PlatonNodeInfo;
import com.cicdi.jcli.service.FastHttpService;

import java.io.IOException;
import java.util.Collections;

/**
 * 一些额外实现的web3j功能
 *
 * @author haypo
 * @date 2021/1/7
 */
public class Web3jUtil {
    /**
     * 获得节点信息
     *
     * @param httpService http服务
     * @return 节点信息
     */
    public static Request<?, PlatonNodeInfo> getNodeInfo(Web3jService httpService) {
        return new Request<>(
                "admin_nodeInfo",
                Collections.<String>emptyList(),
                httpService,
                PlatonNodeInfo.class);
    }

    /**
     * 获得已连接节点
     *
     * @param httpService http服务
     * @return 节点信息
     */
    public static Request<?, Peers> getPeers(Web3jService httpService) {
        return new Request<>(
                "admin_peers",
                Collections.<String>emptyList(),
                httpService,
                Peers.class);
    }

    /**
     * 获得节点信息
     *
     * @param rpcAddress 节点rpc地址
     * @return 节点信息
     */
    public static Request<?, PlatonNodeInfo> getNodeInfo(String rpcAddress) {
        return new Request<>(
                "admin_nodeInfo",
                Collections.<String>emptyList(),
                new FastHttpService(rpcAddress),
                PlatonNodeInfo.class);
    }

    /**
     * 获取blsPubKey
     *
     * @param httpService http服务
     * @return blsPubKey
     */
    public static String getBlsPubKey(Web3jService httpService) {
        try {
            return getNodeInfo(httpService).send().getNodeInfo().getBlsPubKey();
        } catch (Exception e) {
            throw new RuntimeException("can not get bls pub key");
        }
    }

    public static String getNodeId(String rpcAddress) throws IOException {
        return getNodeInfo(rpcAddress).send().getNodeInfo().getId();
    }
}
