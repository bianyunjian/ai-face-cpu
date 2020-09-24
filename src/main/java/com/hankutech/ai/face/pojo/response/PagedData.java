package com.hankutech.ai.face.pojo.response;

import lombok.Data;

import java.util.List;

/**
 * 分页数据返回值封装
 *
 * @author ZhangXi
 */
@Data
public class PagedData<T> {

    /**
     * 分页数据集合
     */
    private List<T> list;

    /**
     * 分页数据总数
     */
    private Long total;

}
