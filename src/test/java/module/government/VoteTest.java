package module.government;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/17
 */
public class VoteTest {
    public static String stakingWalletJson = "E:\\codes\\IdeaProjects\\block_chain_git\\trunk\\platon_util\\keystore_core/staking.json";

    @Test
    public void testVote() {
        String[] argv = "government_vote -t".split(" ");
        Main.main(argv);
    }

    @Test
    public void testVoteOnline() {
        String[] argv = ("government_vote -d " + stakingWalletJson + " -p param/vote.json -c config/macu1_config.json").split(" ");
        Main.main(argv);
    }

    @Test
    public void testVoteOffline() {
        String[] argv = "government_vote -d atp1gyc36n094emzg8ecmpx0wj0hadtnx5wvlz0l6q -p param/vote.json -c config/macu1_config.json".split(" ");
        Main.main(argv);
    }
}
