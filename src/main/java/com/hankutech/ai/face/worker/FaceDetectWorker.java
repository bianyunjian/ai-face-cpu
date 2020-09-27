package com.hankutech.ai.face.worker;

import com.ar.face.faceenginesdk.struct.model.ImageInfo;
import com.ar.face.faceenginesdk.struct.model.detect.FaceDetectOutParam;
import com.ar.face.faceenginesdk.struct.model.register.FaceRegisterOutParam;
import com.hankutech.ai.face.constant.Common;
import com.hankutech.ai.face.support.BaseUtils;
import com.hankutech.ai.face.support.ImageUtil;
import com.hankutech.ai.face.support.face.FaceEngineWrapper;

public class FaceDetectWorker {


    public static byte[] detectFace(String imageDataBase64) {

        imageDataBase64 = ImageUtil.removeImagePrefix(imageDataBase64);

        ImageInfo imgInfo = new ImageInfo();
        imgInfo.base64Data4RGB = imageDataBase64;
        FaceDetectOutParam resp = FaceEngineWrapper.getInstance().detect_Native( imgInfo);

        if (resp.ar_error_code == 0 && resp.faceDetectInfoArray != null && resp.faceDetectInfoArray.length > 0) {



//            return BaseUtils.faceFeature2blob(faceFtrArray);
        }
        return null;
    }
}
