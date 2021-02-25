package module.query;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/18
 */
public class GetPackageRewardTest {

    @Test
    public void testGetPackageReward() {
        String[] args = "query_getPackageReward".split(" ");
        Main.main(args);
    }
}
