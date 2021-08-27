package module.account;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/3/1
 */
public class CheckTest {
    @Test
    public void test() {
        String[] argv = {"account_check"
        };
        Main.main(argv);
    }

    @Test
    public void test2() {
        String[] argv = {"account_check",
                "--address", "atp1r584yu4d7rcs7t9an9yve7ylfm6p8jcwvxnl55"
        };
        Main.main(argv);
    }
    @Test
    public void test4() {
        String[] argv = {"account_check",
                "--address", "wallet6.json"
        };
        Main.main(argv);
    }
    @Test
    public void test3() {
        String[] argv = {"account_check",
                "--address", "123"
        };
        Main.main(argv);
    }
}
