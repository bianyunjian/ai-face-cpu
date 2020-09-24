package com.hankutech.ai.face.support.face;

import com.ar.face.faceenginesdk.FaceEngineSDKWrapper;
import com.ar.face.faceenginesdk.enums.AR_COLOR_MODE;
import com.ar.face.faceenginesdk.enums.AliveDetectorMode;
import com.ar.face.faceenginesdk.enums.ExtractMode;
import com.ar.face.faceenginesdk.struct.model.FaceEnginePara;
import com.ar.face.faceenginesdk.struct.model.FaceRegInfo4InitRegister;
import com.ar.face.faceenginesdk.struct.model.ImageInfo;
import com.ar.face.faceenginesdk.struct.model.detect.FaceDetectInParam;
import com.ar.face.faceenginesdk.struct.model.detect.FaceDetectOutParam;
import com.ar.face.faceenginesdk.struct.model.init.FaceRegisterInitInParam;
import com.ar.face.faceenginesdk.struct.model.init.FaceRegisterInitOutParam;
import com.ar.face.faceenginesdk.struct.model.recognition.FaceRecognitionInParam;
import com.ar.face.faceenginesdk.struct.model.recognition.FaceRecognitionOutParam;
import com.ar.face.faceenginesdk.struct.model.register.FaceRegisterInParam;
import com.ar.face.faceenginesdk.struct.model.register.FaceRegisterOutParam;
import com.sun.jna.Pointer;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class FaceEngineNativeLibrary {

    private static final Logger logger = LoggerFactory.getLogger(FaceEngineNativeLibrary.class);

    private static Pointer faceInstance;

    private static FaceEngineConfigParam faceEngineConfigParam;

    @Getter
    private static boolean initFinished = false;

    public static void config(FaceEngineConfigParam param) {
        faceEngineConfigParam = param;
    }

    public static boolean init(String configPath, String logFilePath) {

        initFinished = false;

        long create_startMills = java.time.Clock.systemDefaultZone().millis();
        logger.debug("createFaceEngine start time=" + create_startMills);
        faceInstance = FaceEngineSDKWrapper.createFaceEngineAndInit(configPath, logFilePath);
        long create_finishedMills = java.time.Clock.systemDefaultZone().millis();
        logger.debug("createFaceEngine finished time=" + create_finishedMills);
        logger.debug("createFaceEngine cost time=" + (create_finishedMills - create_startMills));

        initFinished = true;
        return initFinished;
    }

    public static boolean initRegister(HashMap<String, FaceRegisterOutParam> faceRegisterMap) {

        long init_startMills = java.time.Clock.systemDefaultZone().millis();
        logger.debug("initFaceRegisterInfoGlobal start time=" + init_startMills);
        FaceRegisterInitOutParam outParam = FaceEngineSDKWrapper.initFaceRegisterInfoGlobal(faceInstance, mockRegisterInitInParam(faceRegisterMap));
        long init_finishedMills = java.time.Clock.systemDefaultZone().millis();
        logger.debug("initFaceRegisterInfoGlobal finished time=" + init_finishedMills);
        logger.debug("initFaceRegisterInfoGlobal cost time=" + (init_finishedMills - init_startMills));

        initFinished = faceRegisterMap == null || (outParam != null && outParam.faceRegInfoNums == faceRegisterMap.size());
        return initFinished;
    }

    public static FaceRegisterOutParam getRegisterData(String personId, ImageInfo imgInfo) {

        long startMills = java.time.Clock.systemDefaultZone().millis();
        logger.debug("register start time=" + startMills);

        FaceRegisterOutParam registerOutParam = FaceEngineSDKWrapper.register(faceInstance, mockFaceRegisterInParam(imgInfo));

        if (registerOutParam.ar_err_code == 0) {
            if (registerOutParam.faceRegInfoArray != null && registerOutParam.faceRegInfoArray.length > 0) {
                registerOutParam.faceRegInfoArray[0].personId = personId;
            }

        }

        long finishedMills = java.time.Clock.systemDefaultZone().millis();
        logger.debug("register finished time=" + finishedMills);
        logger.debug("register cost time=" + (finishedMills - startMills));
        return registerOutParam;
    }

    public static FaceDetectOutParam detect(ImageInfo imgInfo) {

        long detect_startMills = java.time.Clock.systemDefaultZone().millis();
        logger.debug("detect start time=" + detect_startMills);

        FaceDetectOutParam faceDetectOutParam = FaceEngineSDKWrapper.detect(faceInstance, mockDetectInParam(imgInfo));

        long detect_finishedMills = java.time.Clock.systemDefaultZone().millis();
        logger.debug("detect finished time=" + detect_finishedMills);
        logger.debug("detect cost time=" + (detect_finishedMills - detect_startMills));
        return faceDetectOutParam;
    }

    public static FaceRecognitionOutParam recognition(ImageInfo imgInfo) {

        long recognition_startMills = java.time.Clock.systemDefaultZone().millis();
        logger.debug("recognition start time=" + recognition_startMills);

        FaceRecognitionOutParam faceRecognitionOutParam = FaceEngineSDKWrapper.recognition(faceInstance, mockRecognitionInParam(imgInfo));

        long recognition_finishedMills = java.time.Clock.systemDefaultZone().millis();
        logger.debug("recognition finished time=" + recognition_finishedMills);
        logger.debug("recognition cost time=" + (recognition_finishedMills - recognition_startMills));
        return faceRecognitionOutParam;
    }

    private static FaceRecognitionInParam mockRecognitionInParam(ImageInfo imgInfo) {
        FaceRecognitionInParam inParam = new FaceRecognitionInParam();
        inParam.width = imgInfo.width4RGB;
        inParam.height = imgInfo.height4RGB;
        inParam.imgDataBuffer = imgInfo.base64Data4RGB;
        inParam.colorMode = AR_COLOR_MODE.AR_COLOR_BGR.getEnumValue();
        inParam.facePara = new FaceEnginePara();
        inParam.extractMode = ExtractMode.EXTRACT_FULL.getEnumValue();
        inParam.numFaces = 0;
        inParam.aliveDetectDataBuffer = imgInfo.base64Data4Depth;
        inParam.aliveDetectWidth = imgInfo.width4Depth;
        inParam.aliveDetectHeight = imgInfo.height4Depth;
        inParam.aliveDetectColorMode = AR_COLOR_MODE.AR_COLOR_BGR.getEnumValue();
        if (imgInfo.base64Data4Depth == null || imgInfo.base64Data4Depth.length() == 0) {
            inParam.aliveDetectMode = AliveDetectorMode.ALIVE_DETECT_NULL.getEnumValue();
        } else {
            inParam.aliveDetectMode = AliveDetectorMode.ALIVE_DETECT_DEPTH.getEnumValue();
        }

        return inParam;
    }

    private static FaceDetectInParam mockDetectInParam(ImageInfo imgInfo) {
        FaceDetectInParam inParam = new FaceDetectInParam();
        inParam.width = imgInfo.width4RGB;
        inParam.height = imgInfo.height4RGB;
        inParam.imgDataBuffer = imgInfo.base64Data4RGB;
        inParam.colorMode = AR_COLOR_MODE.AR_COLOR_BGR.getEnumValue();
        inParam.facePara = new FaceEnginePara();
        inParam.extractMode = ExtractMode.EXTRACT_FULL.getEnumValue();
        inParam.numFaces = 0;
        inParam.aliveDetectDataBuffer = imgInfo.base64Data4Depth;
        inParam.aliveDetectWidth = imgInfo.width4Depth;
        inParam.aliveDetectHeight = imgInfo.height4Depth;
        inParam.aliveDetectColorMode = AR_COLOR_MODE.AR_COLOR_BGR.getEnumValue();
        if (imgInfo.base64Data4Depth == null || imgInfo.base64Data4Depth.length() == 0) {
            inParam.aliveDetectMode = AliveDetectorMode.ALIVE_DETECT_NULL.getEnumValue();
        } else {
            inParam.aliveDetectMode = AliveDetectorMode.ALIVE_DETECT_DEPTH.getEnumValue();
        }

        return inParam;
    }

    private static FaceRegisterInParam mockFaceRegisterInParam(ImageInfo imgInfo) {
        FaceRegisterInParam inParam = new FaceRegisterInParam();
        inParam.width = imgInfo.width4RGB;
        inParam.height = imgInfo.height4RGB;
        inParam.imgDataBuffer = imgInfo.base64Data4RGB;

        inParam.colorMode = AR_COLOR_MODE.AR_COLOR_BGR.getEnumValue();
        inParam.facePara = new FaceEnginePara();
        inParam.extractMode = ExtractMode.EXTRACT_LARGEST.getEnumValue();
        inParam.numFaces = 0;
        inParam.aliveDetectDataBuffer = null;
        inParam.aliveDetectWidth = 0;
        inParam.aliveDetectHeight = 0;
        inParam.aliveDetectColorMode = AR_COLOR_MODE.AR_COLOR_BGR.getEnumValue();
        if (imgInfo.base64Data4Depth == null || imgInfo.base64Data4Depth.length() == 0) {
            inParam.aliveDetectMode = AliveDetectorMode.ALIVE_DETECT_NULL.getEnumValue();
        } else {
            inParam.aliveDetectMode = AliveDetectorMode.ALIVE_DETECT_DEPTH.getEnumValue();
        }
        return inParam;
    }

    private static FaceRegisterInitInParam mockRegisterInitInParam(HashMap<String, FaceRegisterOutParam> faceRegisterMap) {
        FaceRegisterInitInParam inParam = new FaceRegisterInitInParam();

        if (faceRegisterMap == null) {
            return inParam;
        }
        inParam.totalFaceRegInfoNums = faceRegisterMap.keySet().size();
        inParam.faceRegInfoNums = faceRegisterMap.keySet().size();
        inParam.faceRegInfo = new FaceRegInfo4InitRegister[inParam.faceRegInfoNums];
        Object[] keys = faceRegisterMap.keySet().toArray();
        for (int i = 0; i < inParam.faceRegInfoNums; i++) {
            String personId = (String) keys[i];
            FaceRegisterOutParam data = faceRegisterMap.get(personId);

            FaceRegInfo4InitRegister faceRegInfo = new FaceRegInfo4InitRegister();
            if (data != null && data.faceRegInfoArray != null && data.faceRegInfoArray.length > 0) {
                faceRegInfo.faceFtrArray = data.faceRegInfoArray[0].faceFtrArray;
                faceRegInfo.faceDataInfo = data.faceRegInfoArray[0].faceDataInfo;
                faceRegInfo.personId = data.faceRegInfoArray[0].personId;
            }

            inParam.faceRegInfo[i] = faceRegInfo;
        }
        return inParam;
    }

    public static boolean releaseAndDelete() {
        FaceEngineSDKWrapper.releaseAndDeleteFaceEngine(faceInstance);
        initFinished = false;
        return true;
    }


}
