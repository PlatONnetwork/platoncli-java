package com.cicdi.jcli.util;

import com.alibaba.fastjson.JSON;
import com.cicdi.jcli.template.BaseTemplate4Serialize;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.platon.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

import static joptsimple.internal.Strings.EMPTY;

/**
 * 二维码读写工具
 *
 * @author haypo
 * @date 2020/12/25
 */
@Slf4j
public class QrUtil {
    /**
     * 读取二维码
     *
     * @param file 二维码文件
     * @return 二维码信息
     * @throws IOException       io异常
     * @throws NotFoundException 找不到二维码异常
     */
    public static BaseTemplate4Serialize readQrCodeImage(File file) throws IOException, NotFoundException {
        MultiFormatReader formatReader = new MultiFormatReader();
        //读取指定的二维码文件
        BufferedImage bufferedImage = ImageIO.read(file);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
        //定义二维码参数
        Map<DecodeHintType, Object> hints = new HashMap<>(2);
        hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        Result result = formatReader.decode(binaryBitmap, hints);
        bufferedImage.flush();
        BaseTemplate4Serialize baseTemplate4Serialize = JSON.parseObject(result.getText(), BaseTemplate4Serialize.class);
        baseTemplate4Serialize.setData(uncompress(baseTemplate4Serialize.getData()));
        return baseTemplate4Serialize;
    }

    /**
     * 在桌面生成二维码
     *
     * @param text     待写入二维码数据
     * @param width    二维码宽度
     * @param height   二维码高度
     * @param filename 文件名
     * @throws WriterException 写入异常
     * @throws IOException     io异常
     */
    public static void genQrCodeImageToDeskTop(String text, int width, int height, String filename) throws WriterException, IOException {
        String filePath = getDesktopPath() + "/" + filename;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "JPG", path);
    }

    /**
     * gzip解压缩字符串
     *
     * @param compressedStr 已压缩的字符串
     * @return 压缩的字符串
     */
    public static String uncompress(String compressedStr) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            try (OutputStream outputStream = new InflaterOutputStream(os)) {
                outputStream.write(new BASE64Decoder().decodeBuffer(compressedStr));
            }
            return new String(os.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("解压文本失败:{}", compressedStr, e);
        }
        return EMPTY;
    }

    /**
     * 使用gzip进行压缩
     *
     * @param text 未压缩字符串
     * @return 压缩的字符串
     */
    public static String compress(String text) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(out)) {
                deflaterOutputStream.write(text.getBytes(StandardCharsets.UTF_8));
            }
            return new BASE64Encoder().encode(out.toByteArray());
        } catch (IOException e) {
            log.error("压缩文本失败:{}", text, e);
        }
        return EMPTY;
    }


    /**
     * 获得桌面路径
     *
     * @return 桌面路径
     */
    public static String getDesktopPath() {
        File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
        return desktopDir.getAbsolutePath();
    }

    public static String save2QrCodeImage(String prefix, BaseTemplate4Serialize obj) throws IOException, WriterException {
        String qrCodeImageName = prefix + "@" + TimeUtil.getNanoTime() + ".jpg";
        log.info("二维码原始数据：" + JsonUtil.toPrettyJsonString(obj));
        obj.setData(compress(obj.getData()));
        QrUtil.genQrCodeImageToDeskTop(JSONUtil.toJSONString(obj), 2000, 2000, qrCodeImageName);
        return "已在桌面生成二维码！文件名为：" + qrCodeImageName;
    }

    public static <T> String save2QrCodeImage(String prefix, T obj) throws IOException, WriterException {
        String qrCodeImageName = prefix + "@" + TimeUtil.getNanoTime() + ".png";
        QrUtil.genQrCodeImageToDeskTop(JSONUtil.toJSONString(obj), 1600, 1600, qrCodeImageName);
        return "已在桌面生成二维码！文件名为:" + qrCodeImageName;
    }
}
