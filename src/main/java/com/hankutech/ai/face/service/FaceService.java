package com.hankutech.ai.face.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hankutech.ai.face.service.db.Face;

import java.util.List;

public interface FaceService extends IService<Face> {


    boolean checkPersonNameExist(String personName, List<String> excludeImageIds);
}




