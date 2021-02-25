package module.delegate;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * 查询当前账户地址所委托的节点的NodeId和质押Id
 *
 * @author haypo
 * @date 2021/2/18
 */
public class GetRelatedListByDelAddrTest {
    @Test
    public void testGetDelegateReward() {
        String[] argv = {"delegate_getRelatedListByDelAddr", "-d",
                "atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx"
        };
        Main.main(argv);
    }
}
