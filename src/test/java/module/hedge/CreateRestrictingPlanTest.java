package module.hedge;

import com.cicdi.jcli.Main;
import module.TestCommon;
import org.junit.Test;

public class CreateRestrictingPlanTest {
    @Test
    public void help() {
        String[] args = ("hedge_createRestrictingPlan -help").split(" ");
        Main.main(args);
    }

    @Test
    public void test() {
        String[] args = ("hedge_createRestrictingPlan -p param/CreateRestrictingPlan.json -d " + TestCommon.rewardWalletDir).split(" ");
        Main.main(args);
    }

    @Test
    public void t() {
        String[] args = ("hedge_createRestrictingPlan -t").split(" ");
        Main.main(args);
    }
}
