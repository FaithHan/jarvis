package org.jarvis.misc;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.SneakyThrows;
import org.jarvis.io.StreamCopyUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * https://www.jianshu.com/p/6607e69b1121
 * https://www.cnblogs.com/huanzi-qch/p/10097791.html
 * https://www.iteye.com/blog/ququjioulai-2254382
 */
public class QRCodeUtils {

    private static final int DEFAULT_SIZE = 320;

    /**
     * 返回PNG格式的二维码
     *
     * @param content
     * @return
     */
    @SneakyThrows
    public static byte[] encode(String content) {
        return encode(content, QRCodeFileType.PNG, DEFAULT_SIZE, DEFAULT_SIZE);
    }


    @SneakyThrows
    public static byte[] encode(String content, byte[] logo) {
        byte[] encode = encode(content, QRCodeFileType.PNG, DEFAULT_SIZE, DEFAULT_SIZE);
        BufferedImage qrcode = ImageIO.read(new ByteArrayInputStream(encode));
        BufferedImage result = logoMatrix(qrcode, new ByteArrayInputStream(logo));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(result, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    @SneakyThrows
    public static byte[] encode(String content, QRCodeFileType qrCodeFileType, int width, int height) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        // margin
        hints.put(EncodeHintType.MARGIN, "1");

        BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, qrCodeFileType.name(), outputStream, matrixToImageConfig);
        return outputStream.toByteArray();
        //  To BufferedImage
        //  BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);
        //  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //  ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        //  return byteArrayOutputStream.toByteArray();
    }

    public static void encodeAndDump(String content, OutputStream outputStream) throws IOException {
        byte[] encode = encode(content);
        StreamCopyUtils.copy(encode, outputStream);
    }

    public static void encodeAndDump(String content, String path) throws IOException {
        byte[] encode = encode(content);
        StreamCopyUtils.copy(encode, new FileOutputStream(path));
    }

    /**
     * 解析图片文件上的二维码
     *
     * @param imageInputStream 图片文件流
     * @return 解析的结果，null表示解析失败
     */
    public static String decode(InputStream imageInputStream) {
        try {
            BufferedImage image = ImageIO.read(imageInputStream);

            LuminanceSource luminanceSource = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(luminanceSource);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            hints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);

            Result result = new QRCodeReader().decode(binaryBitmap, hints);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SneakyThrows
    public static String decode(File imageFile) {
        return decode(new FileInputStream(imageFile));
    }

    public static String decode(byte[] imageBytes) {
        return decode(new ByteArrayInputStream(imageBytes));
    }


    /**
     * 二维码添加logo
     *
     * @param matrixImage 源二维码图片
     * @param logoFile    logo图片
     * @return 返回带有logo的二维码图片
     * 参考：https://blog.csdn.net/weixin_39494923/article/details/79058799
     */
    private static BufferedImage logoMatrix(BufferedImage matrixImage, InputStream logoFile) throws IOException {
        // 读取二维码图片，并构建绘图对象
        Graphics2D g2 = matrixImage.createGraphics();

        int matrixWidth = matrixImage.getWidth();
        int matrixHeight = matrixImage.getHeight();

        // 读取Logo图片
        BufferedImage logo = ImageIO.read(logoFile);

        //开始绘制图片
        g2.drawImage(logo, matrixWidth / 5 * 2, matrixHeight / 5 * 2, matrixWidth / 5, matrixHeight / 5, null);//绘制

        BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke);// 设置笔画对象
        //指定弧度的圆角矩形
        RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth / 5 * 2, matrixHeight / 5 * 2, matrixWidth / 5, matrixHeight / 5, 20, 20);
        g2.setColor(Color.white);
        g2.draw(round);// 绘制圆弧矩形

        //设置logo 有一道灰色边框
        BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke2);// 设置笔画对象
        RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth / 5 * 2 + 2, matrixHeight / 5 * 2 + 2, matrixWidth / 5 - 4, matrixHeight / 5 - 4, 20, 20);
        g2.setColor(new Color(128, 128, 128));
        g2.draw(round2);// 绘制圆弧矩形

        g2.dispose();
        matrixImage.flush();
        return matrixImage;
    }

    public enum QRCodeFileType {
        PNG, JPG
    }


}
