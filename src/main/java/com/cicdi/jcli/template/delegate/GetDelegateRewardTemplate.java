package com.cicdi.jcli.template.delegate;

import lombok.Data;

import java.util.List;

/**
 * "address": "" ，
 * "nodeIDs": ["id1","id2"]
 *
 * @author haypo
 * @date 2021/1/8
 */
@Data
public class GetDelegateRewardTemplate {
    private String address;
    private List<String> nodeIds;

}
