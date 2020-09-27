package com.hankutech.ai.face.pojo.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class FaceImageUpdateParams {

    int libraryId;

    @NotEmpty
    String imageId;

    @NotEmpty
    String personName;
    
    @NotEmpty
    String imageDataBase64;
}
