package module.government;

import com.cicdi.jcli.Main;
import module.TestCommon;
import org.junit.Test;

/**
 * 举报双签
 *
 * @author haypo
 * @date 2021/2/18
 */
public class ReportDoubleSignTest {
    /**
     * 模板测试
     */
    @Test
    public void testTemplate() {
        String[] argv = {"government_reportDoubleSign", "-t"
        };
        Main.main(argv);
    }

    /**
     * PREPARE_VOTE
     */
    @Test
    public void testReportDoubleSign() {
        String[] argv = {"government_reportDoubleSign", "-p", "param/government_reportDoubleSign.json",
                "-d", TestCommon.stakingWalletDir
        };
        Main.main(argv);
    }

    /**
     * PREPARE_BLOCK
     */
    @Test
    public void testReportDoubleSign2() {
        String[] argv = {"government_reportDoubleSign", "-p", "param/government_reportDoubleSign2.json",
                "-d", TestCommon.stakingWalletDir
        };
        Main.main(argv);
    }

    /**
     * help测试
     */
    @Test
    public void testHelp() {
        String[] argv = {"government_reportDoubleSign", "-help"
        };
        Main.main(argv);
    }
}
