package com.cicdi.jcli.util;

import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.common.ErrorCode;
import com.platon.protocol.exceptions.TransactionException;

/**
 * @author haypo
 * @date 2021/3/23
 */
public class CallResponseUtil {
    /**
     * 处理CallResponse的一般方法
     *
     * @param callResponse 回调响应
     * @param <T>          响应类型泛型
     * @return 处理结果
     */
    public static <T> String handleCallResponse(CallResponse<T> callResponse) throws TransactionException {
        if (callResponse.isStatusOk()) {
            return Common.SUCCESS_STR + ":\n" + JsonUtil.toPrettyJsonString(callResponse.getData());
        } else {
            String errMsg = ErrorCode.getErrorMsg(callResponse.getCode());
            throw new TransactionException(Common.FAIL_STR + ": " + (StringUtil.isBlank(errMsg)?callResponse.getErrMsg():errMsg));
        }
    }
}
