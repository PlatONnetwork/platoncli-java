package module.staking;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/18
 */
public class UnStakingTest {
    public static String stakingWalletJson = "E:\\codes\\IdeaProjects\\block_chain_git\\trunk\\platon_util\\keystore_core/staking.json";

    @Test
    public void testUnStakingPlaton() {
        String[] args = ("staking_unStaking -d " + stakingWalletJson + " -p param/stakingUnStaking_platon.json -c config/platon_config.json").split(" ");
        Main.main(args);

    }
}
