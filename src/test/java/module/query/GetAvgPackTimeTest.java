package module.query;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/18
 */
public class GetAvgPackTimeTest {

    @Test
    public void testGetAvgPackTime() {
        String[] args = "query_getAvgPackTime".split(" ");
        Main.main(args);
    }

}
