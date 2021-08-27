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

    /**
     * PlatON主网上线提案: https://github.com/PlatONnetwork/PIPs/blob/master/Alaya/PIP-15.md
     * 2021-03-04 21:00:28 INFO
     * Command: java -jar platoncli-java-jar-with-dependencies.jar government_vote -d E:\codes\IdeaProjects\block_chain_git\trunk\platon_util\keystore_core/staking.json -p param/vote/votePip15.json -c {"rpcAddress":"http://192.168.31.97:6257","hrp":"atp","chainId":201018}
     * Result: success, tx hash: 0xd814049688f117f9be60f4196be4612a33a84ee97fa0c0214ad36396750b5a3f
     */
    @Test
    public void testVoteOnlinePIP15() {
        String localConfig = "{\"rpcAddress\":\"http://192.168.31.97:6257\",\"hrp\":\"atp\",\"chainId\":201018}";
        String[] argv = ("government_vote -d " + stakingWalletJson + " -p param/vote/votePip15.json -c " + localConfig).split(" ");
        Main.main(argv);
    }

    @Test
    public void testVoteOffline() {
        String[] argv = "government_vote -d atp1gyc36n094emzg8ecmpx0wj0hadtnx5wvlz0l6q -p param/vote.json -c config/macu1_config.json".split(" ");
        Main.main(argv);
    }
}
