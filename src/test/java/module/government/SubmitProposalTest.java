package module.government;

import com.cicdi.jcli.Main;
import module.TestCommon;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/14
 */
public class SubmitProposalTest {
    @Test
    public void testSubmitProposalTemplate() {
        String[] args = "government_submitProposal -t".split(" ");
        Main.main(args);
    }
    @Test
    public void testSubmitVersionProposalOnline() {
        String[] args = ("government_submitProposal -m VersionProposal -param param/VersionProposal.json -d " + TestCommon.rewardWalletDir).split(" ");
        Main.main(args);
    }
    @Test
    public void testSubmitParamProposalOnline() {
        String[] args = ("government_submitProposal -m ParamProposal -param param/ParamProposal.json -d " + TestCommon.rewardWalletDir).split(" ");
        Main.main(args);
    }

    @Test
    public void testSubmitCancelProposal() {
        String[] args = ("government_submitProposal -m cancel_proposal -param param/CancelProposal.json -o -d "+ TestCommon.myBetWalletDir).split(" ");
        Main.main(args);
    }
}
