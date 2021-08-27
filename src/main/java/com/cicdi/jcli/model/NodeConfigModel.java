package com.cicdi.jcli.model;

import lombok.Data;
import lombok.NonNull;


/**
 * {"rpcAddress": "http://127.0.0.1:6789", "hrp": "atp", "chainId": 201018}
 *
 * @author haypo
 * @date 2020/12/25
 */
@Data
public class NodeConfigModel {
    @NonNull
    private String rpcAddress;
    @NonNull
    private String hrp;
    @NonNull
    private Long chainId;
}
