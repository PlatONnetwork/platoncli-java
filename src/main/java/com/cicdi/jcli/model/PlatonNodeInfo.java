package com.cicdi.jcli.model;

import com.alaya.protocol.core.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

/**
 * @author haypo
 * @date 2021/1/7
 */
public class PlatonNodeInfo extends Response<PlatonNodeInfo.NodeInfo> {

    public NodeInfo getNodeInfo() {
        return getResult();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NodeInfo {

        private String id;
        private String name;
        private String blsPubKey;
        private String enode;
        private String ip;
        private Ports ports;
        private String listenAddr;
        private Protocols protocols;

        @Data
        public static class Ports {
            private Integer discovery;
            private Integer listener;
        }

        @Data
        public static class Protocols {
            private LinkedHashMap<String, ?> cbft;
            private LinkedHashMap<String, ?> platon;
        }
    }
}