package module.staking;

import com.cicdi.jcli.Main;
import module.TestCommon;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/15
 */
public class StakingUpdateTest {

    @Test
    public void testStakingUpdatePlatonOfflineTemp() {
        String[] args = "staking_update -d lax1gyc36n094emzg8ecmpx0wj0hadtnx5wvf3tgtq -p param/stakingUpdate_platon.json -c config/platon_config.json".split(" ");
        Main.main(args);
    }

    @Test
    public void testStakingUpdateTemp() {
        String[] args = "staking_update -t -d atp1gyc36n094emzg8ecmpx0wj0hadtnx5wvlz0l6q -p param/stakingUpdate_platon.json".split(" ");
        Main.main(args);
    }

    @Test
    public void testStakingUpdateOffline() {
        String[] args = "staking_update -d atp1gyc36n094emzg8ecmpx0wj0hadtnx5wvlz0l6q -p param/stakingUpdate_platon.json".split(" ");
        Main.main(args);
    }

    @Test
    public void testStakingUpdatePlaton() {
        String[] args = ("staking_update -d " + TestCommon.stakingWalletDir + " -p param/stakingUpdate_platon.json -c config/platon_config.json").split(" ");
        Main.main(args);
    }

    @Test
    public void testStakingUpdate() {
        String[] args = ("staking_update -d " + TestCommon.stakingWalletDir + " -p param/stakingUpdate.json").split(" ");
        Main.main(args);
    }
}
