package system;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2020/12/29
 */
public class MainTest {
    @Test
    public void testMainHelp() {
        String[] args = "-help".split(" ");
        Main.main(args);
    }

    @Test
    public void testMainV() {
        String[] args = "-v".split(" ");
        Main.main(args);
    }
}
