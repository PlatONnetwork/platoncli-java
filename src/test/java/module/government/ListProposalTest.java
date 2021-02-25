package module.government;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/17
 */
public class ListProposalTest {
    @Test
    public void testListProposal() {
        String[] args = "government_listProposal".split(" ");
        Main.main(args);
    }
}
