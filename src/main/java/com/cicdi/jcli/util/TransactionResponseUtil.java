package com.cicdi.jcli.util;

import com.platon.contracts.ppos.dto.TransactionResponse;
import com.platon.contracts.ppos.dto.common.ErrorCode;
import com.platon.protocol.exceptions.TransactionException;

/**
 * @author haypo
 * @date 2021/3/23
 */
public class TransactionResponseUtil {
    /**
     * 处理交易详情
     *
     * @param transactionResponse 交易详情
     * @return 根据交易详情，返回相应信息
     * @throws TransactionException 若交易详情包含交易错误信息，则抛出交易异常
     */
    public static String handleTxResponse(TransactionResponse transactionResponse) throws TransactionException {
        if (transactionResponse.isStatusOk()) {
            return Common.SUCCESS_STR + ":\n" + transactionResponse.toString();
        } else {
            String errMsg = ErrorCode.getErrorMsg(transactionResponse.getCode());
            throw new TransactionException(StringUtil.isBlank(errMsg) ? transactionResponse.getErrMsg() : errMsg);
        }
    }
}
