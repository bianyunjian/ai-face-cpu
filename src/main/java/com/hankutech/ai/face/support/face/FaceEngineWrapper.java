package com.hankutech.ai.face.support.face;

import cn.hutool.core.lang.Assert;
import com.ar.face.faceenginesdk.struct.model.FaceRegInfo;
import com.ar.face.faceenginesdk.struct.model.ImageInfo;
import com.ar.face.faceenginesdk.struct.model.detect.FaceDetectOutParam;
import com.ar.face.faceenginesdk.struct.model.recognition.FaceRecognitionOutParam;
import com.ar.face.faceenginesdk.struct.model.register.FaceRegisterOutParam;
import com.hankutech.ai.face.pojo.vo.FaceVO;
import com.hankutech.ai.face.support.BaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.List;

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
            boolean initSuccess = true;
            if (getInstance().checkIfReady_Native() == false) {
                initSuccess &= getInstance().init_Native(getInstance().param.getConfigPath(), getInstance().param.getLogFilePath());
            }
            if (initSuccess) {
                logger.info("FaceEngineWrapper init_Native success");

                if (getInstance().param.getFaceRegisterMap() != null) {
                    initSuccess &= getInstance().initRegisterFace_Native(getInstance().param.getFaceRegisterMap());
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


    public static void initNativeSDK(List<FaceVO> faceVOList) throws InvalidObjectException {
        FaceEngineConfigParam configParam = getFaceEngineConfigParam();
        FaceEngineWrapper.initFaceEngineWrapper(configParam);
        if (FaceEngineWrapper.getInstance().checkIfReady_Native() == false) {
            throw new InvalidObjectException("Native SDK初始化失败！");
        }

        if (faceVOList != null && faceVOList.size() > 0) {
            System.out.println("人脸库数量:" + faceVOList.size());
            HashMap<String, FaceRegisterOutParam> faceMap = new HashMap<>();

            for (FaceVO p : faceVOList) {
                FaceRegisterOutParam newParam = new FaceRegisterOutParam();
                newParam.id = p.getId();
                newParam.personName = p.getPersonName();
                newParam.faceRegInfoArray = new FaceRegInfo[1];
                newParam.faceRegInfoArray[0] = new FaceRegInfo();
                newParam.faceRegInfoArray[0].personId = String.valueOf(newParam.id);
                for (int i = 0; i < newParam.faceRegInfoArray[0].faceFtrArray.length; i++) {
//                    System.out.println(p.toString());
                    Assert.notNull(p);
                    Assert.notNull(p.getFaceFeatures());
                    newParam.faceRegInfoArray[0].faceFtrArray[i] = p.getFaceFeatures()[i];
                }
                faceMap.put(p.getId().toString(), newParam);
            }

            boolean initFaceSuccess = FaceEngineWrapper.getInstance().initRegisterFace_Native(faceMap);
            if (initFaceSuccess == false) {
                throw new InvalidObjectException("Native SDK初始化人脸库失败！");
            }
            System.out.println("Native SDK初始化人脸库成功！");
        }
    }

    public static FaceEngineConfigParam getFaceEngineConfigParam() {

        FaceEngineConfigParam configParam = new FaceEngineConfigParam();
        configParam.setConfigPath("");
        configParam.setSupportNativeFaceDetect(true);
        configParam.setSupportNativeFaceRecognize(true);

        String logFilePath = BaseUtils.getRootPath() + "/DebugLog";
        File f = new File(logFilePath);
        if (f.exists() == false) f.mkdirs();
        configParam.setLogFilePath(logFilePath);

        return configParam;
    }
}
