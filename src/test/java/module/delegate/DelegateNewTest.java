package module.delegate;

import com.cicdi.jcli.Main;
import com.cicdi.jcli.util.Common;
import com.cicdi.jcli.util.ConvertUtil;
import com.cicdi.jcli.util.QrUtil;
import com.platon.contracts.ppos.DelegateContract;
import com.platon.contracts.ppos.dto.enums.StakingAmountType;
import com.platon.crypto.Credentials;
import com.platon.crypto.WalletUtils;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.protocol.http.HttpService;
import module.TestCommon;
import org.junit.Test;

import java.util.Scanner;

/**
 * @author haypo
 * @date 2021/1/11
 */
public class DelegateNewTest {
    @Test
    public void testHelp() {
        String[] argv = {"delegate_new",
                "-help"
        };
        Main.main(argv);
    }

    @Test
    public void testTemplate() {
        String[] argv = {"delegate_new",
                "-t"
        };
        Main.main(argv);
    }

    @Test
    public void testDelegateNewOffline() {
        String[] argv = {"delegate_new",
                "-d", "atp1zhe8zecq7evp897azxssudk4t9z0a7emp8fulx",
                "-p", "param/delegateNew.json"
        };
        Main.main(argv);
        String pngPath = QrUtil.getDesktopPath() + "\\" + Main.result.split("：")[1];
        String[] args = ("tx_sendOffline -data " + pngPath + " -d " + TestCommon.rewardWalletDir).split(" ");
        Main.main(args);
    }

    @Test
    public void testDelegateNewOnlineFast() {
        String[] argv = {"delegate_new",
                "-fast",
                "-d", TestCommon.rewardWalletDir,
                "-p", "param/delegateNew.json"
        };
        Main.main(argv);
    }

    @Test
    public void testDelegateNewOnline3() {
        String[] argv = {"delegate_new",
                "-d", TestCommon.rewardWalletDir,
                "-p", "param/delegateNew.json",
//                "-gasPrice", "0"
        };
        Main.main(argv);
    }

    @Test
    public void testDelegateNewOnline() {
        String[] argv = {"delegate_new",
                "-d", TestCommon.rewardWalletDir,
                "-p", "param/delegateNew.json"
        };
        Main.main(argv);
    }

    @Test
    public void testDelegateNewOnline2() throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(new Scanner(System.in).nextLine(), "E:/codes/IdeaProjects/block_chain_git/trunk/platon-util-springboot/keystore_core/reward.json");
        TransactionReceipt receipt = DelegateContract.load(Web3j.build(new HttpService(Common.DEFAULT_NODE_URL)), credentials).delegate(
                "0x89ca7ccb7fab8e4c8b1b24c747670757b9ef1b3b7631f64e6ea6b469c5936c501fcdcfa7fef2a77521072162c1fc0f8a1663899d31ebb1bc7d00678634ef746c", StakingAmountType.FREE_AMOUNT_TYPE, ConvertUtil.hrp2Von("0.1")).send().getTransactionReceipt();
        System.out.println(receipt.toString());
    }

}
