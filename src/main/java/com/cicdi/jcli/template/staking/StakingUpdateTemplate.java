package com.cicdi.jcli.template.staking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * "benefitAddress": "",
 * "nodeId": "",
 * "externalId": "",
 * "nodeName": "",
 * "website": "",
 * "details": "",
 * "perNodeName": "",
 * "delegateRewardPer": 0
 *
 * @author haypo
 * @date 2021/1/7
 */
@Data
public class StakingUpdateTemplate {
    @JsonProperty(required = true)
    private BigInteger delegateRewardPer;
    @JsonProperty(required = true)
    private String benefitAddress;
    private String nodeId;
    @JsonProperty(required = true)
    private String externalId;
    @JsonProperty(required = true)
    private String nodeName;
    @JsonProperty(required = true)
    private String website;
    @JsonProperty(required = true)
    private String details;
}
