package com.hankutech.ai.face.pojo.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class LibraryUpdateParams {
    @Min(1)
    int libraryId;
    @NotEmpty
    String libraryName;
    String description;
}
