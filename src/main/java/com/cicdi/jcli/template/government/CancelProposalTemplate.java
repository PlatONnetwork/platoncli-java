package com.cicdi.jcli.template.government;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import java.math.BigInteger;

/**
 * "verifier": "",
 * "piPid": "",
 * "endVotingRound": 0.00,
 * "canceledProposalId": ""
 *
 * @author haypo
 * @date 2021/1/14
 */
@Data
public class CancelProposalTemplate {
    @JsonProperty(required = true)
    private String verifier;
    @JsonProperty(required = true)
    private String piPid;
    @JsonProperty(required = true)
    private BigInteger endVotingRound;
    @JsonProperty(required = true)
    private String canceledProposalId;
}
