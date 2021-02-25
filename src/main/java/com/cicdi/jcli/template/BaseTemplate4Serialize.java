package com.cicdi.jcli.template;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

/**
 * 用于序列化的BaseTemplate
 *
 * @author haypo
 * @date 2021/1/4
 */
@Data
@AllArgsConstructor
public class BaseTemplate4Serialize {
    private String from;
    private List<String> to;
    private String data;
    private BigInteger nonce;
    private BigInteger value;
    private Long chainId;
    private BigInteger gasLimit;
    private BigInteger gasPrice;
    private boolean fast;
}
