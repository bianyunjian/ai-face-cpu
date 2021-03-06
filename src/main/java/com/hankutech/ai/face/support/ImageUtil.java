package com.hankutech.ai.face.support;

import cn.asr.appframework.utility.file.FileUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageUtil {


    static BASE64Encoder encoder = new sun.misc.BASE64Encoder();

    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();


    public static String imageFileToBase64(String imgFilePath, String imgFormat) {
        File f = new File(imgFilePath);
        try {
            BufferedImage bi = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, imgFormat, baos);
            byte[] bytes = baos.toByteArray();

            return encoder.encodeBuffer(bytes).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String base64ToImageFile(String base64String, String imgFilePath, String imgFormat) {
        try {
            byte[] bytes1 = decoder.decodeBuffer(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            BufferedImage bi1 = ImageIO.read(bais);
            File f1 = new File(imgFilePath);
            FileUtils.checkPath(imgFilePath);
            ImageIO.write(bi1, imgFormat, f1);
            return imgFilePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String removeImagePrefix(String imageDataBase64) {
        if (imageDataBase64.startsWith("data:image")) {
            return imageDataBase64.split(",")[1];
        } else {
            return imageDataBase64;
        }
    }
}
