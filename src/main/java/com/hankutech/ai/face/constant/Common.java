package com.hankutech.ai.face.constant;

import cn.asr.appframework.utility.file.FileUtils;

public class Common {
    /**
     * 服务名称
     */
    public static final String SERVICE_NAME = "ai-server-face-cpu";


    /**
     * 人脸得分的最低阈值
     */
    public static final float MIN_SIMI_SCORE = 0.35f;
    public static final String IMG_FORMAT = "jpg";

    public static String RecognizeDataPathPrefix;
    public static String RegisterDataPathPrefix;

    public static Boolean saveRegisterImage;
    public static String registerImageDir;
    public static Boolean saveRecognizeImage;
    public static String recognizeImageDir;

    public static void init() {
        System.out.println("registerImageDir:" + registerImageDir);
        RegisterDataPathPrefix = registerImageDir;
        if (RegisterDataPathPrefix.endsWith("/") == false) {
            RegisterDataPathPrefix += "/";
        }
        String testFile = RegisterDataPathPrefix + "test.txt";
        FileUtils.checkPath(testFile);

        System.out.println("recognizeImageDir:" + recognizeImageDir);
        RecognizeDataPathPrefix = recognizeImageDir;
        if (RecognizeDataPathPrefix.endsWith("/") == false) {
            RecognizeDataPathPrefix += "/";
        }
        testFile = RecognizeDataPathPrefix + "test.txt";
        FileUtils.checkPath(testFile);
    }


}
