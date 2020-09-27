package com.hankutech.ai.face.support.face;

import com.ar.face.faceenginesdk.struct.model.ImageInfo;
import com.ar.face.faceenginesdk.struct.model.detect.FaceDetectOutParam;
import com.ar.face.faceenginesdk.struct.model.recognition.FaceRecognitionOutParam;
import com.ar.face.faceenginesdk.struct.model.register.FaceRegisterOutParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author yunjian.bian
 */
public class FaceEngineWrapper {

    private static FaceEngineWrapper _faceEngineWrapper = null;

    public static FaceEngineWrapper getInstance() {
        if (_faceEngineWrapper == null) {
            throw new NullPointerException("please init FaceEngineWrapper first!");
        }
        return _faceEngineWrapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(FaceEngineWrapper.class);
    private FaceEngineConfigParam param;

    public static void initFaceEngineWrapper(FaceEngineConfigParam param) {
        _faceEngineWrapper = new FaceEngineWrapper();
        getInstance().updateConfigParam(param);

        if (getInstance().param.isSupportNativeFaceDetect()) {
            boolean initSuccess = false;
            if (getInstance().checkIfReady_Native() == false) {
                initSuccess = getInstance().init_Native(getInstance().param.getConfigPath(), getInstance().param.getLogFilePath());
            }
            if (initSuccess) {
                logger.info("FaceEngineWrapper init_Native success");

                if (getInstance().param.getFaceRegisterMap() != null) {
                    initSuccess = getInstance().initRegisterFace_Native(getInstance().param.getFaceRegisterMap());
                }
                if (initSuccess) {
                    logger.info("FaceEngineWrapper initRegisterFace_Native success");
                } else {
                    logger.info("FaceEngineWrapper initRegisterFace_Native failed");
                }
            } else {
                logger.info("FaceEngineWrapper init_Native failed");
            }
        }
    }

    public void updateConfigParam(FaceEngineConfigParam param) {
        FaceEngineNativeLibrary.config(param);
        getInstance().param = param;
    }

    public boolean checkIfReady_Native() {

        return FaceEngineNativeLibrary.isInitFinished();
    }

    public boolean releaseAndDelete_Native() {

        return FaceEngineNativeLibrary.releaseAndDelete();
    }

    private boolean init_Native(String configPath, String logFilePath) {

        return FaceEngineNativeLibrary.init(configPath, logFilePath);
    }

    public boolean initRegisterFace_Native(HashMap<String, FaceRegisterOutParam> faceRegisterMap) {

        return FaceEngineNativeLibrary.initRegister(faceRegisterMap);
    }

    public FaceRegisterOutParam getRegisterData_Native(String personId, ImageInfo imgInfo) {

        return FaceEngineNativeLibrary.getRegisterData(personId, imgInfo);
    }

    public FaceRecognitionOutParam recognition_Native(ImageInfo imgInfo) {

        return FaceEngineNativeLibrary.recognition(imgInfo);
    }

    public FaceDetectOutParam detect_Native(ImageInfo imgInfo) {

        return FaceEngineNativeLibrary.detect(imgInfo);
    }
}
