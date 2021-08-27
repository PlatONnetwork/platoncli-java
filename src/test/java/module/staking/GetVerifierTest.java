package module.staking;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * 查询当前结算周期的验证人列表
 *
 * @author haypo
 * @date 2021/2/18
 */
public class GetVerifierTest {
    @Test
    public void test() {
        String[] args = "staking_getVerifierList".split(" ");
        Main.main(args);
    }
}
