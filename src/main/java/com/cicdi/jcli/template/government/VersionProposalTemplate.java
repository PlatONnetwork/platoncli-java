package com.cicdi.jcli.template.government;

import lombok.Data;

import java.math.BigInteger;

/**
 * 升级提案
 * "validator": "",
 * "piPid": "",
 * "endVotingRound": 0.00,
 * "newVersion": 0
 *
 * @author haypo
 * @date 2021/1/14
 */
@Data
public class VersionProposalTemplate {
    private String verifier;
    private String piPid;
    private BigInteger endVotingRound;
    private BigInteger newVersion;
}
