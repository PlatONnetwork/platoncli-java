package module.government;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/17
 */
public class GetAccuVerifiersCountTest {

    @Test
    public void testGetAccuVerifiersCount() {
        String[] args = ("government_getAccuVerifiersCount -hash 0x1236cbb365587bf61039903d3926ea6df5fc9a265abf9ac0a68045643d85cd21" +
                " -pid 0x261cf6c0f518aeddffb2aa5536685af6f13f8ba763c77b42f12ce025ef7170ed").split(" ");
        Main.main(args);
    }
}
