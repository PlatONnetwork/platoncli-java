package module.government;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/18
 */
public class CheckDoubleSignTest {
    @Test
    public void testCheckDoubleSign() {
        String[] args = ("government_checkDoubleSign -type VIEW_CHANGE" +
                " -nodeId 0x680b23be9f9b1fa7684086ebd465bbd5503305738dae58146043632aa553c79c6a22330605a74f03e84379e9706d7644a8cbe5e638a70a58d20e7eb5042ec3ca" +
                " -number 2000").split(" ");
        Main.main(args);
    }
}
