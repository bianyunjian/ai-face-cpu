package com.hankutech.ai.face.pojo.vo.face;

import com.hankutech.ai.face.pojo.vo.FaceDetectResultVO;
import lombok.Data;

@Data
public class Landmarks {private RightEye rightEye;
    private Nose nose;
    private RightMouthCorner rightMouthCorner;
    private LeftEye leftEye;
    private LeftMouthCorner leftMouthCorner;
}
