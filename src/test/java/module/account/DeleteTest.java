package module.account;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/3/8
 */
public class DeleteTest {
    @Test
    public void test() {
        String[] argv = {"account_delete",
                "-d","atp1vpqw8jd6z6ck4gfmpld2qyywcur4ncky2rs6z5"
        };
        Main.main(argv);
    }

}
