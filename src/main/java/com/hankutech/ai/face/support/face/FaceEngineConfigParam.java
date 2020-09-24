/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hankutech.ai.face.support.face;

import com.ar.face.faceenginesdk.struct.model.register.FaceRegisterOutParam;
import lombok.Data;

import java.util.HashMap;

/**
 * @author dev
 */
@Data
public class FaceEngineConfigParam {

    boolean requireMinFaceCheck;
    int minFaceWidth;
    int minFaceHeight;

    float minSimiScore;
    String personName4minSimiScore;

    boolean supportNativeFaceDetect;
    boolean supportNativeFaceRecognize;
    boolean supportCloudFaceRecognize;
    boolean supportCloudFaceDetect;
    String configPath;
    HashMap<String, FaceRegisterOutParam> faceRegisterMap = new HashMap<>();
    String logFilePath;
}
