package module.account;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/3/1
 */
public class NewTest {
    @Test
    public void test() {
        String[] argv = {"account_new",
                "-name", "wallet6.json"
        };
        Main.main(argv);
    }

    @Test
    public void test2() {
        String[] argv = {"account_new",
                "-b", "10"
        };
        Main.main(argv);
    }
}
