package module.government;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/11
 */
public class DeclareVersionTest {
    @Test
    public void testHelp() {
        String[] args = "government_declareVersion -help".split(" ");
        Main.main(args);
    }

    @Test
    public void testTemplate() {
        String[] args = "government_declareVersion -t".split(" ");
        Main.main(args);
    }

    @Test
    public void testDeclareVersionAlaya() {
        String[] args = "government_declareVersion -param param/declare_version.json -o -d atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx".split(" ");
        Main.main(args);
    }

    @Test
    public void testDeclareVersionPlaton() {
        String[] argv = {"government_declareVersion",
                "-param", "param/declare_version.json",
                "-d", "atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx",
                "--config", "config/platon_config.json"
        };
        Main.main(argv);
    }
}
