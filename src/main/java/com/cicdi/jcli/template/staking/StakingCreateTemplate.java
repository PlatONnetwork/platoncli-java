package com.cicdi.jcli.template.staking;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * "type": 0,
 * "benefitAddress": "收益地址",
 * "nodeId": "",自动读取当前工作目录下nodeId
 * "externalId": "keybase账户公钥",
 * "nodeName": "节点名称",
 * "website": "节点的第三方主页",
 * "details": "节点描述",
 * "amount": 0.00,
 * "delegateRewardPer": 0
 *
 * @author haypo
 * @date 2021/1/6
 */
@Data
public class StakingCreateTemplate {
    private BigInteger type;
    private String benefitAddress;
    private String nodeId;
    private String externalId;
    private String nodeName;
    private String website;
    private String details;
    /**
     * 质押金额，单位LAT，质押门槛1000000LAT, 10000ATP
     */
    private BigDecimal amount;
    private BigInteger delegateRewardPer;
}
