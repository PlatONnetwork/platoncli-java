package module.tx;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/15
 */
public class GetTransactionTest {

    @Test
    public void testGetTransaction() {
        String[] args = "tx_getTransaction -h 0xd6bfcc4a9811dcafb4a72a74c731dc2054535c49f5b1bb887c3db0cdaefaae69".split(" ");
        Main.main(args);
    }
}
