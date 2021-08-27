package com.cicdi.jcli.util;

import com.cicdi.jcli.contractx.DelegateContractX;
import com.cicdi.jcli.contractx.NodeContractX;
import com.cicdi.jcli.contractx.RewardContractX;
import com.cicdi.jcli.model.WalletFileX;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platon.contracts.ppos.dto.resp.Delegation;
import com.platon.contracts.ppos.dto.resp.DelegationIdInfo;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.contracts.ppos.dto.resp.Reward;
import com.platon.crypto.*;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.utils.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import static com.platon.crypto.Hash.sha256;

/**
 * 钱包工具
 *
 * @author haypo
 * @date 2021/3/1
 */
public class WalletUtil {
    public static final String MAIN_TEST_ADDRESS_REGEX = "\\\"address\\\"\\s*:\\s*\\{\\s*\\\"mainnet\\\"\\s*:\\s*\\\"([A-Za-z0-9]+)\\\"[^}]*\\}";
    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    static {
        if (SecureRandomUtils.isAndroidRuntime()) {
            new LinuxSecureRandom();
        }
    }

    public static WalletFile loadWalletFile(File source, String hrp) throws IOException {
        // 统一把source文件中的address值替换为“{}”，兼容新旧格式钱包文件的加载
        String fileContent = Files.readString(source);
        fileContent = fileContent.replaceAll(MAIN_TEST_ADDRESS_REGEX, "\"address\": \"$1\"");
        WalletFile walletFile = objectMapper.readValue(fileContent, WalletFile.class);

        walletFile.setAddress(AddressUtil.formatHrpAddress(walletFile.getAddress(), hrp));
        return walletFile;
    }

    public static Credentials loadCredentials(String password, String filePath, String hrp) throws IOException, CipherException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("file not found at path: " + filePath);
        }
        return loadCredentials(password, file, hrp);
    }

    public static Credentials loadCredentials(String password, File source, String hrp) throws IOException, CipherException {
        WalletFile walletFile = loadWalletFile(source, hrp);
        Credentials credentials = Credentials.create(Wallet.decrypt(password, loadWalletFile(source, hrp)));
        if (!walletFile.getAddress().equalsIgnoreCase(credentials.getAddress())) {
            throw new CipherException("wallet file's content is cracked.");
        }
        return Credentials.create(Wallet.decrypt(password, loadWalletFile(source, hrp)));
    }

    /**
     * 创建钱包文件和助记词密文备份文件
     *
     * @param password 密码
     * @param file     钱包路径
     * @return 是否创建成功
     */
    public static boolean createWalletFile(String password, File file, String hrp) {
        try {
            WalletFileX wfx = generateBip39Wallet(password, file, hrp);
            StringUtil.info("%s: %s %s:%s",
                    ResourceBundleUtil.getTextString("createWalletFile"),
                    wfx.getFilename(),
                    ResourceBundleUtil.getTextString("address"),
                    wfx.getAddress()
            );
            File walletDir = new File("wallet");
            if (walletDir.mkdirs()) {
                System.out.println(ResourceBundleUtil.getTextString("createWalletDir"));
            }
            String wfxFilename = walletDir.getName() + "/Bip39-" + wfx.getFilename();
            String finalFilename = JsonUtil.writeJsonFileWithNoConflict(wfxFilename, wfx);
            StringUtil.info("%s: %s", ResourceBundleUtil.getTextString("createMnemonicBackupFile"), finalFilename);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获得委托总量
     *
     * @param web3j   web3对象
     * @param hrp     hrp
     * @param address 查询地址
     * @return 委托总量
     * @throws Exception 合约异常
     */
    public static BigInteger getDelegateTotal(Web3j web3j, String hrp, String address) throws Exception {
        DelegateContractX rcx = DelegateContractX.load(web3j, hrp);
        List<DelegationIdInfo> delegationIdInfoList = rcx.getRelatedListByDelAddr(address).send().getData();
        BigInteger delegateTotal = BigInteger.ZERO;
        for (DelegationIdInfo reward : delegationIdInfoList) {
            Delegation delegation = rcx.getDelegateInfo(reward.getNodeId(), address, reward.getStakingBlockNum()).send().getData();
            delegateTotal = delegateTotal.add(delegation.getDelegateLocked().add(delegation.getDelegateReleased()));
        }
        return delegateTotal;
    }

    /**
     * 查询委托收益
     *
     * @param web3j   web3
     * @param hrp     hrp值
     * @param address 地址
     * @return 委托收益
     * @throws Exception 合约异常
     */
    public static BigInteger getDelegateReward(Web3j web3j, String hrp, String address) throws Exception {
        List<Node> nodes = NodeContractX.load(web3j, hrp).getCandidateList().send().getData();
        List<String> nodeIdList = nodes.stream().map(Node::getNodeId).collect(Collectors.toList());
        RewardContractX rcx = RewardContractX.load(web3j, hrp);
        List<Reward> rewardList = rcx.getDelegateReward(address,
                nodeIdList).send().getData();
        BigInteger totalReward = BigInteger.ZERO;
        for (Reward reward : rewardList) {
            totalReward = totalReward.add(reward.getReward());
        }
        return totalReward;
    }

    public static BigInteger getBalance(Web3j web3j, String address) throws IOException {
        return web3j.platonGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
    }

    public static WalletFile genWalletByPrivateKey(String privateKey, String password) throws CipherException {
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        return Wallet.createStandard(password, ecKeyPair);
    }

    public static WalletFile genWalletByMnemonic(String mnemonic, String password) throws CipherException {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        ECKeyPair ecKeyPair = ECKeyPair.create(sha256(seed));
        return Wallet.createStandard(password, ecKeyPair);
    }

    /**
     * 通过创建助记词创建钱包
     *
     * @param password 密码
     * @param file     钱包文件名称
     * @param hrp      hrp值
     * @return 助记词备份文件
     * @throws Exception 创建异常
     *                   {@link WalletUtils#generateBip39Wallet(String, File)}
     */
    private static WalletFileX generateBip39Wallet(String password, File file, String hrp) throws Exception {
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);

        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        ECKeyPair ecKeyPair = ECKeyPair.create(sha256(seed));

        WalletFile walletFile = Wallet.createStandard(password, ecKeyPair);

        String finalFilename = JsonUtil.writeJsonFileWithNoConflict(file.getName(), walletFile);
        return new WalletFileX(
                walletFile.getAddress(),
                finalFilename,
                AesUtil.getAesUtil().encrypt(mnemonic, password),
                hrp
        );
    }
}
