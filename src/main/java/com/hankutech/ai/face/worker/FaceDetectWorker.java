package com.hankutech.ai.face.worker;

import com.ar.face.faceenginesdk.struct.model.ImageInfo;
import com.ar.face.faceenginesdk.struct.model.detect.FaceDetectOutParam;
import com.hankutech.ai.face.support.ImageUtil;
import com.hankutech.ai.face.support.face.FaceEngineWrapper;

public class FaceDetectWorker {

    public static FaceDetectOutParam detect(String imageDataBase64) {
        imageDataBase64 = ImageUtil.removeImagePrefix(imageDataBase64);

        ImageInfo imgInfo = new ImageInfo();
        imgInfo.base64Data4RGB = imageDataBase64;
        return FaceEngineWrapper.getInstance().detect_Native(imgInfo);
    }
}
