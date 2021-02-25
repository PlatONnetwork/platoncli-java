package com.cicdi.jcli.template;

import com.beust.jcommander.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * 用于从json反序列化的BaseTemplate
 *
 * @author haypo
 * @date 2021/1/4
 */
@Data
@AllArgsConstructor
public class BaseTemplate4Deserialize {
    @Parameter(names = "from", description = "发送方地址")
    private String from;
    @Parameter(names = "to", description = "接受方地址")
    private List<String> to;
    @Parameter(names = "data", description = "交易数据")
    private String data;
    @Parameter(names = "nonce", description = "钱包nonce值")
    private BigInteger nonce;
    @Parameter(names = "value", description = "交易金额，单位为hrp")
    private BigDecimal value;
    private Long chainId;
    @Parameter(names = "gasLimit", description = "gas用量限制")
    private BigInteger gasLimit;
    @Parameter(names = "gasPrice", description = "gas价格")
    private BigInteger gasPrice;
    @Parameter(names = "fast", description = "是否使用快速发送")
    private boolean fast;
}
