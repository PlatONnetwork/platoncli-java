package module.staking;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * 查询所有实时候选人列表
 *
 * @author haypo
 * @date 2021/2/18
 */
public class GetCandidateListTest {
    @Test
    public void testGetCandidateList() {
        String[] args = "staking_getCandidateList".split(" ");
        Main.main(args);
    }
}
