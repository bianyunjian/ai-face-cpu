package com.hankutech.ai.face.pojo.vo;


import lombok.Data;

@Data
public class FaceVO {
    private Integer id;

    private String personName;

    private Float[] faceFeatures;

    private String imageUrl;

}
