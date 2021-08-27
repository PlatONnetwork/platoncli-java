package module.staking;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/2/18
 */
public class GetCandidateInfoTest {
    @Test
    public void testGetCandidateInfo() {
        String[] args = "staking_getCandidateInfo -nodeId 0x89ca7ccb7fab8e4c8b1b24c747670757b9ef1b3b7631f64e6ea6b469c5936c501fcdcfa7fef2a77521072162c1fc0f8a1663899d31ebb1bc7d00678634ef746c".split(" ");
        Main.main(args);
    }

    @Test
    public void testGetAllCandidateInfo() {
        String[] args = "staking_getCandidateInfo".split(" ");
        Main.main(args);
    }
}
