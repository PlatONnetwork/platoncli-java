package module.hedge;

import com.cicdi.jcli.Main;
import org.junit.Test;

public class GetRestrictingInfoTest {
    @Test
    public void help() {
        String[] args = ("hedge_GetRestrictingInfo -help").split(" ");
        Main.main(args);
    }

    @Test
    public void test() {
        String[] args = ("hedge_GetRestrictingInfo -d atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx").split(" ");
        Main.main(args);
    }
}
