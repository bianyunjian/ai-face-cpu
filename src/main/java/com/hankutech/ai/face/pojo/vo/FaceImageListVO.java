package com.hankutech.ai.face.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class FaceImageListVO {
    private int totalCount;
    private List<FaceImageVO> libraryList;

}
