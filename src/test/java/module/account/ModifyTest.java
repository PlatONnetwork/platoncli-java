package module.account;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/3/13
 */
public class ModifyTest {
    @Test
    public void test() {
        String[] argv = {"account_modify",
                "-d", "atp1hahyr5gj0zl399njfwmmjd2yz0nxl5x60hxuz5"
        };
        Main.main(argv);
    }

}
