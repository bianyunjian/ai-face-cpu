package com.hankutech.ai.face.pojo.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class FaceRecognizeParams {

    int libraryId;
    @NotEmpty
    String imageDataBase64;

}
