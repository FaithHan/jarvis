package org.jarvis.image;

import org.jarvis.http.HttpSiteFactory;
import org.jarvis.io.FileCopyUtils;
import org.jarvis.io.FileTypeUtils;
import org.jarvis.io.StreamCopyUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class ImageUtilsTest {

    @Test
    void isImage() {
//        String imagePath = "/Users/hanfei08/Desktop/UFO待关单列表.xlsx";
        String imagePath = "/Users/hanfei08/Desktop/码神进阶营结业证书.jpeg";
//        String imagePath = "/Users/hanfei08/Desktop/131628246419_.pic_thumb.jpg";
//        String imagePath = "/Users/hanfei08/Desktop/8552.gif";
        System.out.println(ImageUtils.isImage(new File(imagePath)));
        String imageFileType = ImageUtils.getImageFileType(new File(imagePath));
        System.out.println(imageFileType);
//        BufferedImage myPicture = ImageIO.read(new File(imagePath));
//        new MyFrame();
    }

    @Test
    void convertFormat() throws IOException {
        byte[] bytes = FileCopyUtils.copyToByteArray(new File("/Users/hanfei08/Desktop/131628246419_.pic_thumb.jpg"));
        byte[] bytes1 = ImageUtils.convertFormat(bytes, ImageFormat.GIF);
        FileCopyUtils.copy(bytes1,new File("/Users/hanfei08/Desktop/a.gif"));
        System.out.println(FileTypeUtils.getFileType(bytes));
        System.out.println(FileTypeUtils.getFileType(bytes1));
    }

    @Test
    void compressImageTest() throws IOException {
        String imagePath = "/Users/hanfei08/Desktop/码神进阶营结业证书.jpeg";
        byte[] bytes = ImageUtils.compressImage(StreamCopyUtils.copyToByteArray(new FileInputStream(imagePath)),1.0);
        FileCopyUtils.copy(bytes, new File("/Users/hanfei08/Desktop/a.jpg"));
    }

    @Test
    void cutImageTest() throws Exception {
//        byte[] bytes = StreamCopyUtils.copyToByteArray(new FileInputStream("/Users/hanfei08/Desktop/8552.gif"));
        byte[] bytes = HttpSiteFactory.custom()
                .proxy("localhost",6152)
                .build()
                .get("https://i.pinimg.com/originals/68/a1/11/68a111a78aca8260020e5560d5c8a9ff.jpg", byte[].class).getData();
        byte[] bytes1 = ImageUtils.cutImage(bytes, 1, 1);
        byte[] bytes2 = ImageUtils.compressImage(bytes, 100, 100);
        FileCopyUtils.copy(bytes2,new File("/Users/hanfei08/Desktop/a."+ ImageUtils.getImageFileType(bytes)));
    }
}