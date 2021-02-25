package com.cicdi.jcli.model;

import com.alaya.protocol.core.Response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author haypo
 * @date 2021/1/26
 */
public class Peers extends Response<List<Peers.Peer>> {
    public List<Peers.Peer> getPeers() {
        return getResult();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Peer {
        private String id;
        private String name;
        private String blsPubKey;
        private List<String> caps;
        private Peer.Network network;
        private Peers.Peer.Protocols protocols;

        @Data
        public static class Network {
            private String localAddress;
            private String remoteAddress;
            private Boolean inbound;
            private Boolean trusted;
            @JsonProperty(value = "static")
            private Boolean static0;
            private String consensus;
        }

        @Data
        public static class Protocols {
            private LinkedHashMap<String, ?> cbft;
            private LinkedHashMap<String, ?> platon;
        }
    }
}
