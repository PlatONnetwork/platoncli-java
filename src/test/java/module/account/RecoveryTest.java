package module.account;

import com.platon.crypto.CipherException;
import com.platon.crypto.Credentials;
import com.platon.crypto.WalletUtils;
import com.cicdi.jcli.Main;
import com.cicdi.jcli.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author haypo
 * @date 2021/3/13
 */
public class RecoveryTest {
    @Test
    public void testMnemonic() {
        String[] argv = {"account_recovery",
                "-t", "mnemonic"
        };
        Main.main(argv);
    }

    @Test
    public void testP() {
        String[] argv = {"account_recovery",
                "-t", "p"
        };
        Main.main(argv);
    }

    @Test
    public void testWallet() throws IOException, CipherException {
        Credentials c1 = WalletUtils.loadCredentials(StringUtil.readPassword(), new File("atp1g8z640msyg7pd85wjwdh8dpn4eevg4385u5rk2.mnemonic.recovery.json"));
        Credentials c2 = WalletUtils.loadCredentials(StringUtil.readPassword(), "atp1g8z640msyg7pd85wjwdh8dpn4eevg4385u5rk2.privateKey.recovery.json");
        Assert.assertEquals(c1.getEcKeyPair().getPrivateKey(), c2.getEcKeyPair().getPrivateKey());
    }
}
