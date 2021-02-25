package module.government;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/17
 */
public class ListGovernParamTest {

    @Test
    public void testListGovernParam() {
        String[] args = "government_listGovernParam -m staking".split(" ");
        Main.main(args);
    }
}
