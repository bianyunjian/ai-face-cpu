package com.hankutech.ai.face.pojo.vo.face;

import lombok.Data;

@Data
public class FaceInfo {
    private Landmarks landmarks;
    private FaceRect faceRect;
    private float similarityScore;
    private String personName;
}