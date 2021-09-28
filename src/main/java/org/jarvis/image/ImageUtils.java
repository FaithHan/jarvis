package org.jarvis.image;

import net.coobird.thumbnailator.Thumbnails;
import org.jarvis.io.FileCopyUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * Java缩放图片大小：<p>
 * //https://www.baeldung.com/java-resize-image
 */
public class ImageUtils {

    /**
     * 判断一个文件是不是图片
     *
     * @param file
     * @return
     */
    public static boolean isImage(File file) {
        try {
            return isImage(FileCopyUtils.copyToByteArray(file));
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isImage(byte[] imgBytes) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imgBytes));
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            return width > 0 && height > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getImageFileType(byte[] imgBytes) {
        if (isImage(imgBytes)) {
            try {
                ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(imgBytes));
                Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
                if (!iter.hasNext()) {
                    return null;
                }
                ImageReader reader = iter.next();
                iis.close();
                return reader.getFormatName().toLowerCase();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static String getImageFileType(File f) {
        try {
            return getImageFileType(FileCopyUtils.copyToByteArray(f));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 任意格式图片转其它格式
     *
     * @param srcImgBytes
     * @param destImgType
     * @return
     */
    public static byte[] convertFormat(byte[] srcImgBytes, ImageFormat destImgType) {
        BufferedImage bufferedImage;
        try {
            // read image file
            bufferedImage = ImageIO.read(new ByteArrayInputStream(srcImgBytes));
            // create a blank, RGB, same width and height, and a white
            // background
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            // TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            // write to jpeg file
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(newBufferedImage, destImgType.name(), outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 按比例压缩图片
     *
     * @param srcImgBytes 源图片bytes
     * @param scale       老图片pixel * scale = 新图片pixel
     * @return
     * @throws IOException
     */
    public static byte[] compressImage(byte[] srcImgBytes, double scale) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(new ByteArrayInputStream(srcImgBytes))
                .scale(scale)
                .outputQuality(1.0)
                .outputFormat(getImageFileType(srcImgBytes))
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }

    /**
     * 压缩图片(会保留原始图片比例，如果强制尺寸使用forceSize)
     *
     * @param srcImgBytes 源图片bytes
     * @param width       宽
     * @param height      高
     * @return
     * @throws IOException
     */
    public static byte[] compressImage(byte[] srcImgBytes, int width, int height) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(new ByteArrayInputStream(srcImgBytes))
                .size(width, height)
                .outputQuality(1.0)
                .outputFormat(getImageFileType(srcImgBytes))
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }

    /**
     * 裁剪图片
     *
     * @param srcImgBytes 源图片bytes
     * @param widthRatio  宽比
     * @param heightRatio 长比
     * @return 裁剪后图片bytes
     */
    public static byte[] cutImage(byte[] srcImgBytes, int widthRatio, int heightRatio) throws Exception {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(srcImgBytes));
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        double widthSale = (double) width / widthRatio;
        double heightSale = (double) height / heightRatio;

        int subWidth;
        int subHeight;
        if (widthSale == heightSale) {
            return Arrays.copyOf(srcImgBytes,srcImgBytes.length);
        } else if (widthSale > heightSale) {
            subWidth = (int) (heightSale * widthRatio);
            subHeight = height;
        } else {
            subWidth = width;
            subHeight = (int) (widthSale * heightRatio);
        }
        int x = (width - subWidth) / 2;
        int y = (height - subHeight) / 2;
        return cutImage(srcImgBytes, x, y, subWidth, subHeight);
    }

    /**
     * 裁剪图片
     *
     * @param srcImgBytes 源图片bytes
     * @param x           横轴坐标
     * @param y           竖轴坐标
     * @param w           宽度
     * @param h           高度
     * @return 裁剪后图片bytes
     */
    public static byte[] cutImage(byte[] srcImgBytes, int x, int y, int w, int h) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(srcImgBytes));
        BufferedImage subimage = bufferedImage.getSubimage(x, y, w, h);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(subimage, Objects.requireNonNull(getImageFileType(srcImgBytes)), output);
        return output.toByteArray();
    }


}
