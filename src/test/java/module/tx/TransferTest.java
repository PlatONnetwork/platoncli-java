package module.tx;

import com.cicdi.jcli.Main;
import lombok.extern.slf4j.Slf4j;
import module.TestCommon;
import org.junit.Test;

/**
 * @author haypo
 * @date 2020/12/25
 */
@Slf4j
public class TransferTest {

    @Test
    public void testPrintHelp2() {
        String[] args = "tx_transfer -help".split(" ");
        Main.main(args);
    }


    @Test
    public void testTransferTemplate() {
        String[] args = "tx_transfer -template".split(" ");
        Main.main(args);
    }


    @Test
    public void testTransfer() {
        String[] args = ("tx_transfer -p param/transfer1.json -d " + TestCommon.rewardWalletDir).split(" ");
        Main.main(args);
    }

    @Test
    public void testTransferFast() {
        String[] args = ("tx_transfer -fast -p param/transfer2.json -d " + TestCommon.rewardWalletDir).split(" ");
        Main.main(args);
    }

    @Test
    public void testTransferFastOffline() {
        String[] args = ("tx_transfer -fast -o -p param/transfer1.json -d " + TestCommon.rewardWalletDir).split(" ");
        Main.main(args);
    }

    @Test
    public void testTransferFast2() {
        String para = "param/transfer2.json";
        String config = "{\"rpcAddress\":\"https://openapi.alaya.network/rpc\",\"hrp\":\"atp\",\"chainId\":201018}";
        String config2 = "{\n" +
                "  \"rpcAddress\": \"https://openapi.alaya.network/rpc\",\n" +
                "  \"hrp\": \"atp\",\n" +
                "  \"chainId\": 201018\n" +
                "}".replaceAll(" ", "").replace(' ', '\0').replace(' ', '\0');
        String[] args = ("tx_transfer -fast -p " + para + " -d " + TestCommon.rewardWalletDir + " -c " + config).split(" ");
        Main.main(args);
    }
}
