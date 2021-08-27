package module.account;

import com.cicdi.jcli.Main;
import org.junit.Test;

/**
 * @author haypo
 * @date 2021/3/12
 */
public class BackupsTest {
    @Test
    public void testHelp() {
        String[] argv = {"account_backups",
                "--help"
        };
        Main.main(argv);
    }

    @Test
    public void testBackupsMnemonic() {
        String[] argv = {"account_backups",
                "-t", "mnemonic",
                "-d", "wallet6.json"
        };
        Main.main(argv);
    }

    @Test
    public void testBackupsPrivateKey() {
        String[] argv = {"account_backups",
                "-t", "privateKey",
                "-d", "wallet6.json"
        };
        Main.main(argv);
    }

    @Test
    public void testBackupsMnemonicWithAddress() {
        String[] argv = {"account_backups",
                "-t", "mnemonic",
                "-d", "atp1g8z640msyg7pd85wjwdh8dpn4eevg4385u5rk2"
        };
        Main.main(argv);
    }

    @Test
    public void testBackupsPrivateKeyWithAddress() {
        String[] argv = {"account_backups",
                "-t", "privateKey",
                "-d", "atp1g8z640msyg7pd85wjwdh8dpn4eevg4385u5rk2"
        };
        Main.main(argv);
    }
}
