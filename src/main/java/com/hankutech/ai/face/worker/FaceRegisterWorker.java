package com.hankutech.ai.face.worker;

import com.ar.face.faceenginesdk.struct.model.ImageInfo;
import com.ar.face.faceenginesdk.struct.model.register.FaceRegisterOutParam;
import com.hankutech.ai.face.constant.Common;
import com.hankutech.ai.face.support.BaseUtils;
import com.hankutech.ai.face.support.ImageUtil;
import com.hankutech.ai.face.support.face.FaceEngineWrapper;

public class FaceRegisterWorker {
    public static String saveFaceImage(String imageDataBase64, int libraryId, String personName) {

        if (Common.saveRegisterImage == false) return "";
        imageDataBase64 = ImageUtil.removeImagePrefix(imageDataBase64);

        String imgFormat = Common.IMG_FORMAT;
        String imgFilePath = Common.RegisterDataPathPrefix + libraryId + "/" + personName + "." + imgFormat;
        return ImageUtil.base64ToImageFile(imageDataBase64, imgFilePath, imgFormat);
    }

    public static byte[] getFaceFeatures(String imageDataBase64) {

        imageDataBase64 = ImageUtil.removeImagePrefix(imageDataBase64);

        String personId = "1";
        ImageInfo imgInfo = new ImageInfo();
        imgInfo.base64Data4RGB = imageDataBase64;
        FaceRegisterOutParam resp = FaceEngineWrapper.getInstance().getRegisterData_Native(personId, imgInfo);

        if (resp.ar_err_code == 0 && resp.faceRegInfoArray != null && resp.faceRegInfoArray.length > 0) {
            float[] faceFtrArray = resp.faceRegInfoArray[0].faceFtrArray;

            return BaseUtils.faceFeature2blob(faceFtrArray);
        }
        return null;
    }
}
