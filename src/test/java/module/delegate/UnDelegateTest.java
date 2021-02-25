package module.delegate;

import com.cicdi.jcli.Main;
import com.cicdi.jcli.util.QrUtil;
import module.TestCommon;
import org.junit.Test;

/**
 * 减持/撤销委托
 *
 * @author haypo
 * @date 2021/2/18
 */
public class UnDelegateTest {
    /**
     * 模板测试
     */
    @Test
    public void testTemplate() {
        String[] argv = {"delegate_unDelegate", "-t"
        };
        Main.main(argv);
    }

    /**
     * help测试
     */
    @Test
    public void testHelp() {
        String[] argv = {"delegate_unDelegate", "-help"
        };
        Main.main(argv);
    }

    @Test
    public void testOnline() {
        String[] argv = {"delegate_unDelegate", "-d", TestCommon.rewardWalletDir,
                "-p", "param/delegate_unDelegate.json"
        };
        Main.main(argv);
    }

    @Test
    public void testOnlineFast() {
        String[] argv = {"delegate_unDelegate", "-fast", "-d", TestCommon.rewardWalletDir,
                "-p", "param/delegate_unDelegate.json", "-gasLimit", "123456", "-gasPrice", "1000000001"
        };
        Main.main(argv);
    }

    @Test
    public void testOffline() {
        String[] argv = {"delegate_unDelegate", "-fast", "-d", "atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx",
                "-p", "param/delegate_unDelegate.json"
        };
        Main.main(argv);
    }

    /**
     * 2021-02-18 15:28:06 INFO [com.cicdi.jcli.submodule.tx.SendOfflineSubmodule] operation: sendOffline, mode: normal, from: atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx, txHash: 0xe3e387e1db3abc6d6689bf1fe190be9cc113411f52072f1375ffc1c030614071, transactionContent: 9837306,blockNumber: 9837306, nodeRpcAddress: https://openapi.alaya.network/rpc
     * 2021-02-18 15:28:06 INFO [com.cicdi.jcli.Main]
     * Command: tx_sendOffline
     * Result: success
     */
    @Test
    public void testSendOffline() {
        testOffline();
        String pngPath = QrUtil.getDesktopPath() + "\\" + Main.result.split(":")[1];
        String[] args = {
                "tx_sendOffline", "-data", pngPath, "-d", TestCommon.rewardWalletDir
        };
        Main.main(args);
    }
}
