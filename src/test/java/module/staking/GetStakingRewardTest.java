package module.staking;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * 查询当前结算周期的质押奖励
 *
 * @author haypo
 * @date 2021/2/18
 */
public class GetStakingRewardTest {
    @Test
    public void testGetStakingReward() {
        String[] args = "staking_getStakingReward".split(" ");
        Main.main(args);
    }
}
