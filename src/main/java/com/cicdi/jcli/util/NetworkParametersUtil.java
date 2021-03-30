package com.cicdi.jcli.util;

import com.platon.bech32.Bech32;
import lombok.Data;

/**
 * 根据不同的hrp生成对应的ppos合约地址
 *
 * @author haypo
 * @date 2021/1/4
 */
@Data
public class NetworkParametersUtil {

    private NetworkParametersUtil() {
    }


    public static String getPposContractAddressOfStaking(String hrp) {
        return Bech32.addressEncode(hrp, Common.PPOS_CONTRACT_HEX_ADDRESS_OF_STAKING);
    }

    public static String getPposContractAddressOfIncentivePool(String hrp) {
        return Bech32.addressEncode(hrp, Common.PPOS_CONTRACT_HEX_ADDRESS_OF_INCENTIVE_POOL);
    }

    public static String getPposContractAddressOfProposal(String hrp) {
        return Bech32.addressEncode(hrp, Common.PPOS_CONTRACT_HEX_ADDRESS_OF_PROPOSAL);
    }

    public static String getPposContractAddressOfRestrictingPlan(String hrp) {
        return Bech32.addressEncode(hrp, Common.PPOS_CONTRACT_HEX_ADDRESS_OF_RESTRICTING_PLAN);
    }

    public static String getPposContractAddressOfReward(String hrp) {
        return Bech32.addressEncode(hrp, Common.PPOS_CONTRACT_HEX_ADDRESS_OF_REWARD);
    }

    public static String getPposContractAddressOfSlash(String hrp) {
        return Bech32.addressEncode(hrp, Common.PPOS_CONTRACT_HEX_ADDRESS_OF_SLASH);
    }
}
