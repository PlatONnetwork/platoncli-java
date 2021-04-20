package system;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/4/20
 */
public class TestHelpTemplate {
    private static final String[] cmdArray = {
            "account_modify", "staking_getCandidateList", "account_sign", "staking_getVerifierList", "account_check", "government_submitProposal",
            "staking_increase", "government_getProposal", "tx_transfer", "staking_getStakingReward", "government_checkDoubleSign",
            "government_declareVersion", "query_getAvgPackTime", "account_backups", "hedge_createRestrictingPlan", "account_recovery",
            "tx_sendOffline", "account_delete", "delegate_getDelegateReward", "tx_getTransactionReceipt", "account_getBalance",
            "staking_create", "government_listGovernParam", "staking_getValidatorList", "government_reportDoubleSign",
            "government_getAccuVerifiersCount", "staking_getStakingInfo", "staking_update", "delegate_unDelegate",
            "government_vote", "delegate_getRelatedListByDelAddr", "government_getActiveVersion", "query_getBlockByNumber",
            "query_blockNumber", "query_getPackageReward", "account_new", "staking_unStaking", "government_getTallyResult",
            "government_getGovernParamValue", "hedge_getRestrictingInfo", "government_listProposal", "staking_getCandidateInfo",
            "query_getBlockByHash", "delegate_new", "delegate_withdrawDelegateReward", "tx_getTransaction"
    };

    @Test
    public void test() {
//        Main.main("-help");
        for (String cmd : cmdArray) {
            Main.main(cmd, "-help");
        }
    }
}
