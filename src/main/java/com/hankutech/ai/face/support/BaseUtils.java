package com.hankutech.ai.face.support;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BaseUtils {

    static final Logger logger = LoggerFactory.getLogger(BaseUtils.class);

    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }

    public static boolean isArm64() {
        String arch = System.getProperty("os.arch");
        logger.debug("arch:" + arch);
        return arch.toUpperCase().indexOf("AARCH64") > -1;
    }

    public static void checkEnv() {
        logger.debug(BaseUtils.isWindows() ? "windows" : "non-windows");
        String arch = System.getProperty("os.arch");
        logger.debug("arch:" + arch);
        String dataModel = System.getProperty("sun.arch.data.model", System.getProperty("com.ibm.vm.bitmode"));
        logger.debug("dataModel:" + dataModel);
        String code = new File("/lib/arm-linux-gnueabihf").isDirectory() ? "hf" : "el";
        logger.debug("code:" + code);
    }

    public static String getRootPath() {
        String path = new File("").getAbsolutePath();
        return path;
    }

    public static String readResourceAsString(String path) {
        InputStream in = null;
        try {
            File file = new File(path);
            in = new FileInputStream(file);
            byte[] contentBytes = new byte[(int) file.length()];
            in.read(contentBytes);
            String contentString = new String(contentBytes, "utf-8");
            return contentString;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
        }
        return "";
    }

    public static void copyStreamToFile(InputStream inputStream, String outputPath) {
        try {
            FileOutputStream outputStream = new FileOutputStream(outputPath);

            int bufferSize = 10240;
            byte[] b = new byte[bufferSize];
            int readCount = 0;
            int offset = 0;
            do {
                readCount = inputStream.read(b, offset, bufferSize);
                if (readCount > 0) {
                    outputStream.write(b, 0, readCount);
                }
            } while (readCount > 0);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static byte[] faceFeature2blob(float[] features) {
        String fearureString = "";
        if (features != null && features.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (float f : features
            ) {
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append(String.valueOf(f));
            }
            fearureString = builder.toString();
        }

        return fearureString.getBytes(Charsets.UTF_8);
    }

    public static Float[] blob2faceFeature(byte[] blob) {
        if (blob != null && blob.length > 0) {
            String fearureString = new String(blob, Charsets.UTF_8);
            if (StringUtils.isEmpty(fearureString) == false) {
                String[] itemArray = fearureString.split(",");
                List<Float> resp = new ArrayList<>(itemArray.length);
                for (String item : itemArray
                ) {
                    resp.add(Float.parseFloat(item));
                }

                return resp.toArray(new Float[]{});
            }
        }
        return null;
    }

    public static String generateRecordId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
