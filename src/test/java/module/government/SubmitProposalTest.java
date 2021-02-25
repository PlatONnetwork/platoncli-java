package module.government;

import com.cicdi.jcli.Main;
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
    public void testSubmitCancelProposal() {
        String[] args = "government_submitProposal -m cancel_proposal -param param/CancelProposal.json -d atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx".split(" ");
        Main.main(args);
    }
}
