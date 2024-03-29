package com.cicdi.jcli.util;

import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.common.FunctionType;
import com.platon.contracts.ppos.exception.NoSupportFunctionType;
import com.platon.contracts.ppos.utils.EstimateGasUtil;
import com.platon.protocol.Web3j;
import com.platon.tx.gas.ContractGasProvider;
import com.platon.tx.gas.GasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.platon.protocol.core.JsonRpc2_0Web3j.DEFAULT_BLOCK_TIME;

/**
 * 存储一些常用常量
 *
 * @author haypo
 * @date 2020/12/22
 */
public class Common {
    public static final int TWO = 2;
    public static final String JSON_SUFFIX = ".json";
    public static final String LETTER_Y = "y";
    public static final int MAX_NODE_ID_LENGTH = 130;
    public static final int MIN_NODE_ID_LENGTH = 128;
    public static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 40;
    public static final long DEFAULT_POLLING_FREQUENCY = DEFAULT_BLOCK_TIME;
    /**
     * 锁仓合约地址
     */
    public static final String PPOS_CONTRACT_HEX_ADDRESS_OF_RESTRICTING_PLAN = "0x1000000000000000000000000000000000000001";
    /**
     * 质押合约地址
     */
    public static final String PPOS_CONTRACT_HEX_ADDRESS_OF_STAKING = "0x1000000000000000000000000000000000000002";
    /**
     * 激励池地址
     */
    public static final String PPOS_CONTRACT_HEX_ADDRESS_OF_INCENTIVE_POOL = "0x1000000000000000000000000000000000000003";
    /**
     * 惩罚合约地址
     */
    public static final String PPOS_CONTRACT_HEX_ADDRESS_OF_SLASH = "0x1000000000000000000000000000000000000004";
    /**
     * 治理合约地址
     */
    public static final String PPOS_CONTRACT_HEX_ADDRESS_OF_PROPOSAL = "0x1000000000000000000000000000000000000005";
    /**
     * 委托收益合约地址
     */
    public static final String PPOS_CONTRACT_HEX_ADDRESS_OF_REWARD = "0x1000000000000000000000000000000000000006";
    public static final String CANCEL_STR = ResourceBundleUtil.getTextString("cancel");
    public static final String SUCCESS_STR = ResourceBundleUtil.getTextString("success");
    public static final String FAIL_STR = ResourceBundleUtil.getTextString("fail");
    public static final String HELP_TEXT = "Usage: java -jar platon-jcli-jar-with-dependencies.jar [Submodule/Command] [COMMAND] [ARGS]...\n\n" +
            "    platoncli 工具\n" +
            "    --help -h     帮助\n" +
            "    --version -v  版本号";
    public static final String DEFAULT_CONFIG = "config/node_config.json";
    public static final String PROGRAM_NAME = "java -jar platon-jcli-jar-with-dependencies.jar";
    public final static String DEFAULT_NODE_URL = "https://openapi.alaya.network/rpc";
    public static final String HTTP_REGEX = "(http|https):\\/\\/([\\w.]+\\/?)\\S*";
    public final static BigInteger MID_GAS_PRICE = BigInteger.valueOf(1000000000L);
    public final static BigInteger MID_GAS_LIMIT = BigInteger.valueOf(100000L);
    public final static BigInteger TX_GAS_LIMIT = BigInteger.valueOf(21000L);
    public static final GasProvider MID_GAS_PROVIDER = new GasProvider() {
        @Override
        public BigInteger getGasPrice() {
            return ConvertUtil.hrp2Von("0.0001");
        }

        @Override
        public BigInteger getGasLimit() {
            return BigInteger.valueOf(100000L);
        }
    };
    /**
     * 并发池
     */
    public static final ExecutorService EXECUTOR = new ThreadPoolExecutor(
            //cpu核心数
            Runtime.getRuntime().availableProcessors(),
            Integer.MAX_VALUE,
            0, TimeUnit.MILLISECONDS,
            // 使用有界队列，避免OOM
            new ArrayBlockingQueue<>(10000),
            new ThreadPoolExecutor.DiscardPolicy());


    /**
     * 获得默认的gas提供器
     *
     * @param function 合约方法
     * @return gas提供器
     * @throws NoSupportFunctionType 不支持的方法
     */
    public static GasProvider getDefaultGasProvider(Function function) throws NoSupportFunctionType {
        if (EstimateGasUtil.isSupportLocal(function.getType())) {
            return getDefaultGasProviderLocal(function);
        } else {
            return new GasProvider() {
                @Override
                public BigInteger getGasPrice() {
                    return MID_GAS_PRICE;
                }

                @Override
                public BigInteger getGasLimit() {
                    return MID_GAS_LIMIT;
                }
            };
        }
    }

    private static GasProvider getDefaultGasProviderLocal(Function function) throws NoSupportFunctionType {
        BigInteger gasLimit = EstimateGasUtil.getGasLimit(function);
        BigInteger gasPrice;
        switch (function.getType()) {
            case FunctionType.SUBMIT_TEXT_FUNC_TYPE:
                gasPrice = BigInteger.valueOf(1500000).multiply(BigInteger.valueOf(1000000000));
                break;
            case FunctionType.SUBMIT_VERSION_FUNC_TYPE:
                gasPrice = BigInteger.valueOf(2100000).multiply(BigInteger.valueOf(1000000000));
                break;
            case FunctionType.SUBMIR_PARAM_FUNCTION_TYPE:
                gasPrice = BigInteger.valueOf(2000000).multiply(BigInteger.valueOf(1000000000));
                break;
            case FunctionType.SUBMIT_CANCEL_FUNC_TYPE:
                gasPrice = BigInteger.valueOf(3000000).multiply(BigInteger.valueOf(1000000000));
                break;
            default:
                gasPrice = Common.MID_GAS_PRICE;
                break;
        }
        return new ContractGasProvider(gasPrice, gasLimit);
    }


}