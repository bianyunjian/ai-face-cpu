package com.hankutech.ai.face.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class FaceLibraryListVO {
    private int totalCount;
    private List<FaceLibraryVO> libraryList;

}
