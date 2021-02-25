package module.staking;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/27
 */
public class StakingIncreaseTest {
    @Test
    public void testStakingIncrease(){
        String[] args = "staking_increase -t".split(" ");
        Main.main(args);
    }
}
