package module.government;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/17
 */
public class GetGovernParamValueTest {
    @Test
    public void testGetGovernParamValue() {
        String[] args = "government_getGovernParamValue -p param/getGovernParamValue.json".split(" ");
        Main.main(args);
    }
}
