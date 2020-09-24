package com.hankutech.ai.face.pojo.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LibraryCreateParams {
    @NotEmpty
    String libraryName;
    String description;
}
