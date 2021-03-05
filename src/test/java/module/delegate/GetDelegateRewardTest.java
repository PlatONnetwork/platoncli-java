package module.delegate;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * 查询账户在各节点未提取委托奖励
 *
 * @author haypo
 * @date 2021/2/18
 */
public class GetDelegateRewardTest {
    @Test
    public void testHelp() {
        String[] argv = {"delegate_getDelegateReward",
                "-help"
        };
        Main.main(argv);
    }

    /**
     * 模板测试
     */
    @Test
    public void testTemplate() {
        String[] argv = {"delegate_getDelegateReward", "-t"
        };
        Main.main(argv);
    }

    @Test
    public void testGetDelegateReward() {
        String[] argv = {"delegate_getDelegateReward", "-p",
                "param/delegate_getDelegateReward.json"
        };
        Main.main(argv);
    }
}
