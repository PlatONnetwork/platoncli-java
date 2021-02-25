package module.query;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/18
 */
public class GetBlockByHashTest {
    @Test
    public void testGetBlockByHash(){
        String[] args = "query_getBlockByHash -h 0x33daf11b5b29c6106effc76508409a7c1a2872af90d9e564d6cd6e4bb40e5ace".split(" ");
        Main.main(args);
    }
}
