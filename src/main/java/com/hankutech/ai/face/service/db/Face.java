package com.hankutech.ai.face.service.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "face")
public class Face {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "face_library_id")
    private Integer faceLibraryId;

    @TableField(value = "person_name")
    private String personName;

    @TableField(value = "image_url")
    private String imageUrl;

    @TableField(value = "features")
    private byte[] features;

    public static final String COL_ID = "id";

    public static final String COL_FACE_LIBRARY_ID = "face_library_id";

    public static final String COL_PERSON_NAME = "person_name";

    public static final String COL_IMAGE_URL = "image_url";

    public static final String COL_FEATURES = "features";
}