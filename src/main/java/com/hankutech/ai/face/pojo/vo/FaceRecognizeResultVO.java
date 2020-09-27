package com.hankutech.ai.face.pojo.vo;

import com.hankutech.ai.face.pojo.vo.face.FaceInfo;
import lombok.Data;

import java.util.List;

@Data
public class FaceRecognizeResultVO {
    private int faceNum;
    private List<FaceInfo> faceInfoList;

}
