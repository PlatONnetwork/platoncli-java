package com.cicdi.jcli.util;

import com.platon.contracts.ppos.dto.TransactionResponse;
import com.platon.contracts.ppos.dto.common.ErrorCode;
import com.platon.protocol.core.methods.response.Log;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.protocol.exceptions.TransactionException;
import com.platon.rlp.solidity.RlpDecoder;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.rlp.solidity.RlpType;
import com.platon.utils.Numeric;

import java.util.List;

/**
 * 交易回执处理
 *
 * @author haypo
 * @date 2021/3/23
 */
public class TransactionReceiptUtil {
    /**
     * 处理交易回执
     *
     * @param transactionReceipt 交易回执
     * @return 根据交易回执，返回相应信息
     * @throws TransactionException 若交易回执包含交易错误信息，则抛出交易异常
     */
    public static String handleTxReceipt(TransactionReceipt transactionReceipt) throws TransactionException {
        if (transactionReceipt.isStatusOK()) {
            return Common.SUCCESS_STR + ":\n" + transactionReceipt;
        } else {
            TransactionResponse transactionResponse = getResponseFromTransactionReceipt(transactionReceipt);
            String errMsg = ErrorCode.getErrorMsg(transactionResponse.getCode());
            throw new TransactionException(StringUtil.isBlank(errMsg) ? transactionResponse.getErrMsg() : errMsg);
        }
    }

    /**
     * 根据交易回执查询交易详情
     *
     * @param transactionReceipt 交易回执
     * @return 交易详情
     * @throws TransactionException 交易异常
     */
    protected static TransactionResponse getResponseFromTransactionReceipt(TransactionReceipt transactionReceipt) throws TransactionException {
        List<Log> logs = transactionReceipt.getLogs();
        if (logs == null || logs.isEmpty()) {
            throw new TransactionException("TransactionReceipt logs is empty");
        }

        String logData = logs.get(0).getData();
        if (null == logData || "".equals(logData)) {
            throw new TransactionException("TransactionReceipt log data is empty");
        }

        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logData));
        List<RlpType> rlpList = ((RlpList) (rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString) rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setCode(statusCode);
        transactionResponse.setTransactionReceipt(transactionReceipt);

        return transactionResponse;
    }
}
