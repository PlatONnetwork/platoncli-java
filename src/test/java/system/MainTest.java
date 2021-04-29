package system;

import com.Ostermiller.util.CSVParser;
import com.cicdi.jcli.Main;
import com.cicdi.jcli.util.ResourceBundleUtil;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;


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

    @Test
    public void test() throws IOException {
        ResourceBundleUtil.printTemplate("GetDelegateRewardSubmodule", Locale.CHINA);
    }

}
