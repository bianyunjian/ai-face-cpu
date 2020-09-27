package com.hankutech.ai.face.pojo.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class FaceImageAddParams {

    int libraryId;

    @NotEmpty
    String personName;

    @NotEmpty
    String imageDataBase64;
}
