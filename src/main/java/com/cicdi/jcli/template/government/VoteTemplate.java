package com.cicdi.jcli.template.government;

import com.platon.contracts.ppos.dto.enums.VoteOption;
import lombok.Data;

/**
 * "verifier": "",
 * "proposalId": "",
 * "option": ""
 *
 * @author haypo
 * @date 2021/1/14
 */
@Data
public class VoteTemplate {
    private String verifier;
    private String proposalId;
    private VoteOption option;
}
