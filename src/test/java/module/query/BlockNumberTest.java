package module.query;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/18
 */
public class BlockNumberTest {
    @Test
    public void testPrintHelp1() {
        String[] args = "query_blockNumber -help".split(" ");
        Main.main(args);
    }

    @Test
    public void testQueryPlaton() {
        String[] args = "query_blockNumber".split(" ");
        Main.main(args);
    }

}
