package module.tx;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/2/22
 */
public class GetTransactionReceiptTest {
    @Test
    public void testGetTransactionReceipt() {
        String[] args = "tx_getTransactionReceipt -h 0x49d06123a744b3a0db0cd9c119d60b95f711e06c7837cc47f4a46dcddf8dca10".split(" ");
        Main.main(args);
    }

    @Test
    public void testPrintHelp3() {
        String[] args = "tx_getTransactionReceipt -help".split(" ");
        Main.main(args);
    }
}
