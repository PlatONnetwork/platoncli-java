package module.delegate;

import com.cicdi.jcli.Main;
import com.cicdi.jcli.util.QrUtil;
import module.TestCommon;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/1/11
 */
public class WithdrawDelegateRewardTest {
    @Test
    public void testWithdrawDelegateRewardOffline() {
        String[] argv = {"delegate_withdrawDelegateReward", "-f",
                "-d", "atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx",
        };
        Main.main(argv);
    }

    @Test
    public void testWithdrawDelegateReward() {
        String[] argv = {"delegate_withdrawDelegateReward",
                "-d", TestCommon.rewardWalletDir, "-gasLimit", "123456", "-gasPrice", "2000000000"
        };
        Main.main(argv);
    }

    @Test
    public void testWithdrawDelegateRewardFast() {
        String[] argv = {"delegate_withdrawDelegateReward", "-f",
                "-d", TestCommon.rewardWalletDir, "-gasLimit", "123456", "-gasPrice", "1000000001"
        };
        Main.main(argv);
    }

    @Test
    public void testWithdrawDelegateRewardFastWithQrCode() {
        String[] argv = {"delegate_withdrawDelegateReward", "-f",
                "-d", "atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx",
        };
        Main.main(argv);
        String pngPath = QrUtil.getDesktopPath() + "\\" + Main.result.split(":")[1];
        String[] args = ("tx_sendOffline -data " + pngPath + " -d E:/codes/IdeaProjects/block_chain_git/trunk/alaya-util-springboot/keystore_core/reward.json").split(" ");
        Main.main(args);
    }
}
