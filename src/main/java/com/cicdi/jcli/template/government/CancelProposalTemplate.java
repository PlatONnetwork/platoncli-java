package com.cicdi.jcli.template.government;

import lombok.Data;

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
    private String verifier;
    private String piPid;
    private BigInteger endVotingRound;
    private String canceledProposalId;
}
