package module.government;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/2/19
 */
public class GetProposalTest {
    @Test
    public void test() {
        String[] args = "government_getProposal -pid 0xf61e717687fb76ef097b7078f9ea6723dd30926ec754ffc4db266b57461b1011".split(" ");
        Main.main(args);
    }
}
