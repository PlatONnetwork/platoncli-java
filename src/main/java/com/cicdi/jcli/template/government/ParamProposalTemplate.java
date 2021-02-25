package com.cicdi.jcli.template.government;

import lombok.Data;

/**
 * 参数提案
 * "verifier": "",
 * "piPid": "",
 * "module": "",
 * "name": "",
 * "newValue": ""
 *
 * @author haypo
 * @date 2021/1/14
 */
@Data
public class ParamProposalTemplate {
    private String verifier;
    private String piPid;
    private String module;
    private String name;
    private String newValue;
}
