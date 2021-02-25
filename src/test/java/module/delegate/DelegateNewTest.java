package module.delegate;

import com.cicdi.jcli.Main;
import com.cicdi.jcli.util.QrUtil;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/11
 */
public class DelegateNewTest {
    @Test
    public void testDelegateNewOffline() {
        String[] argv = {"delegate_new",
                "-d", "atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx",
                "-p", "param/delegateNew.json"
        };
        Main.main(argv);
        String pngPath = QrUtil.getDesktopPath() + "\\" + Main.result.split(":")[1];
        String[] args = ("tx_sendOffline -data " + pngPath + " -d E:/codes/IdeaProjects/block_chain_git/trunk/alaya-util-springboot/keystore_core/reward.json").split(" ");
        Main.main(args);
    }

    @Test
    public void testDelegateNewOnlineFast() {
        String[] argv = {"delegate_new",
                "-fast",
                "-d", "E:/codes/IdeaProjects/block_chain_git/trunk/alaya-util-springboot/keystore_core/reward.json",
                "-p", "param/delegateNew.json"
        };
        Main.main(argv);
    }

    @Test
    public void testDelegateNewOnline() {
        String[] argv = {"delegate_new",
                "-d", "E:/codes/IdeaProjects/block_chain_git/trunk/alaya-util-springboot/keystore_core/reward.json",
                "-p", "param/delegateNew.json"
        };
        Main.main(argv);
    }
}
