package system;

import com.cicdi.jcli.Main;
import org.junit.Test;

public class TestAllTemplate {
    private static final String[] cmdWithTemplate = {
            "government_vote", "government_submitProposal", "government_declareVersion", "government_getGovernParamValue", "government_reportDoubleSign",
            "delegate_new", "delegate_unDelegate", "delegate_getDelegateReward",
            "hedge_createRestrictingPlan",
            "staking_create", "staking_increase", "staking_unStaking", "staking_update"
    };

    @Test
    public void test() {
        for (String cmd : cmdWithTemplate) {
            Main.main(cmd, "-template");
        }
    }
}
