package com.hankutech.ai.face.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hankutech.ai.face.pojo.vo.FaceVO;
import com.hankutech.ai.face.service.db.FaceLibrary;

import java.util.List;

public interface FaceLibraryService extends IService<FaceLibrary> {

    List<FaceVO> getFaceList(int faceLibraryId);

    boolean checkFaceLibraryExist(int libraryId);
}





