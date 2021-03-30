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
        String[] args = ("government_checkDoubleSign -type 3" +
                " -nodeId 0x6f5584a27a272099c1c8dd948f24f09b660ca38918e76db0eb3fad3c177b97229775ef61d45341279317bce68cf08fce30473dcfd30a0348049b0248e29a3" +
                " -number 2000").split(" ");
        Main.main(args);
    }

    @Test
    public void testCheckDoubleSignError() {
        String[] args = ("government_checkDoubleSign -type NO_TYPE" +
                " -nodeId 0x680b23be9f9b1fa7684086ebd465bbd5503305738dae58146043632aa553c79c6a22330605a74f03e84379e9706d7644a8cbe5e638a70a58d20e7eb5042ec3ca" +
                " -number 2000").split(" ");
        Main.main(args);
    }
}
