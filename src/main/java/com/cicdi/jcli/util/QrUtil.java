package com.cicdi.jcli.util;

import com.alaya.utils.JSONUtil;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码读写工具
 *
 * @author haypo
 * @date 2020/12/25
 */
public class QrUtil {
    public static Result readQrCodeImage(File file) throws IOException, NotFoundException {
        MultiFormatReader formatReader = new MultiFormatReader();
        //读取指定的二维码文件
        BufferedImage bufferedImage = ImageIO.read(file);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
        //定义二维码参数
        Map<DecodeHintType, Object> hints = new HashMap<>(2);
        hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        Result result = formatReader.decode(binaryBitmap, hints);
        bufferedImage.flush();
        return result;
    }

    public static Result readQrCodeImage(String fileSrc) throws IOException, NotFoundException {
        return readQrCodeImage(new File(fileSrc));
    }

    private static void generateQrCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "JPG", path);
    }

    public static void genQrCodeImageToDeskTop(String text, int width, int height, String fileName) throws WriterException, IOException {
        generateQrCodeImage(text, width, height, getDesktopPath() + "/" + fileName);
    }

    public static String getDesktopPath() {
        File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
        return desktopDir.getAbsolutePath();
    }

    public static void main(String[] args) {
        try {
//            generateQrCodeImage("This is my first QR Code", 350, 350, getDesktopPath() + "/My.png");

            //输出相关的二维码信息
            Result result = readQrCodeImage(getDesktopPath() + "/" + "TxTransfer@2020-12-28@17-24-27-874.png");
            System.out.println("解析结果：" + result.toString());
            System.out.println("二维码格式类型：" + result.getBarcodeFormat());
            System.out.println("二维码文本内容：" + result.getText());

//            TransferTemplate transferTemplate = JSONUtil.parseObject(result.toString(), TransferTemplate.class);
//            System.out.println(transferTemplate.toString());
        } catch (Exception e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }
    }

    public static <T> String save2QrCodeImage(String prefix, T obj) throws IOException, WriterException {
        String qrCodeImageName = prefix + "@" + TimeUtil.getNanoTime() + ".png";
        QrUtil.genQrCodeImageToDeskTop(JSONUtil.toJSONString(obj), 700, 700, qrCodeImageName);
        return "已在桌面生成二维码！文件名为:" + qrCodeImageName;
    }
}
