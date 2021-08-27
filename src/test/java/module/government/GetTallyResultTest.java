package module.government;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/2/19
 */
public class GetTallyResultTest {
    @Test
    public void test() {
        String[] args = "government_getTallyResult -pid 0x81a704420dfdafb9056ad1b85066d896899186dac4ce5f72753c8e74131841bc".split(" ");
        Main.main(args);
    }
}
