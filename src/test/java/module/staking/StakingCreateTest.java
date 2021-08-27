package module.staking;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/14
 */
public class StakingCreateTest {
    public static String stakingWalletJson = "E:\\codes\\IdeaProjects\\block_chain_git\\trunk\\platon_util\\keystore_core/staking.json";

    @Test
    public void testStakingCreate() {
        String[] args = "staking_create -d atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx -p param/staking_new.json".split(" ");
        Main.main(args);
    }

    @Test
    public void testStakingCreateOffline() {
        String[] args = "staking_create -d atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx -p param/staking_new.json".split(" ");
        Main.main(args);
    }

    @Test
    public void testStakingCreatePlaton() {
        String[] args = ("staking_create -d " + stakingWalletJson + " -p param/stakingCreate_platon.json -c config/platon_config.json").split(" ");
        Main.main(args);
    }

    @Test
    public void testStakingCreateTemplate() {
        String[] args = "staking_create -t".split(" ");
        Main.main(args);
    }
}
