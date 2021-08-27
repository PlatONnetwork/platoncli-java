package module.account;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/3/15
 */
public class BalanceTest {
    @Test
    public void test() {
        String[] argv = {"account_getBalance",
                "-d", "atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx"
        };
        Main.main(argv);
    }
}
