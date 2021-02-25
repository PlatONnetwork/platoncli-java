package module.query;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/18
 */
public class GetBlockByNumberTest {

    @Test
    public void testGetBlockByNumberPlaton() {
        String[] args = "query_getBlockByNumber -n 100".split(" ");
        Main.main(args);
    }
}
