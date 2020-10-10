package com.hankutech.ai.face.service.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "face_library")
public class FaceLibrary {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "product_id")
    private Integer productId;

    @TableField(value = "name")
    private String name;

    @TableField(value = "description")
    private String description;

    public static final String COL_ID = "id";

    public static final String COL_PRODUCT_ID = "product_id";

    public static final String COL_NAME = "name";

    public static final String COL_DESCRIPTION = "description";
}