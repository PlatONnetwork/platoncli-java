package module.tx;

import com.cicdi.jcli.Main;
import com.cicdi.jcli.util.QrUtil;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/2/18
 */
public class SendOfflineTest {

    @Test
    public void testTransferOffline() {
        String[] args = "tx_transfer -d atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx -p param/transfer1.json".split(" ");
        Main.main(args);
    }

    @Test
    public void testSendOffline() {
        testTransferOffline();
        String pngPath = QrUtil.getDesktopPath() + "\\" + Main.result.split("：")[1];
        String[] args = ("tx_sendOffline -data " + pngPath + " -d E:/codes/IdeaProjects/block_chain_git/trunk/alaya-util-springboot/keystore_core/reward.json").split(" ");
        Main.main(args);
    }
}
