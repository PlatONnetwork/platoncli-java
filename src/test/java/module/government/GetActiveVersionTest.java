package module.government;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/17
 */
public class GetActiveVersionTest {
    @Test
    public void testGetActiveVersion() {
        String[] args = "government_getActiveVersion".split(" ");
        Main.main(args);
    }
}
