package com.hankutech.ai.face.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hankutech.ai.face.pojo.vo.FaceVO;
import com.hankutech.ai.face.service.db.Face;
import com.hankutech.ai.face.service.db.FaceLibrary;
import com.hankutech.ai.face.service.db.FaceLibraryMapper;
import com.hankutech.ai.face.support.BaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FaceLibraryServiceImpl extends ServiceImpl<FaceLibraryMapper, FaceLibrary> implements FaceLibraryService {

    @Autowired
    FaceService faceService;

    @Override
    public List<FaceVO> getFaceList(int faceLibraryId) {

        QueryWrapper<FaceLibrary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(FaceLibrary.COL_ID, faceLibraryId);
        FaceLibrary faceLibraryRecord = getOne(queryWrapper);
        if (faceLibraryRecord == null) {
            log.error("找不到指定的人脸库faceLibraryId:" + faceLibraryId);
            return null;
        }


        QueryWrapper<Face> queryWrapper2 = new QueryWrapper<Face>();
        queryWrapper2.eq(Face.COL_FACE_LIBRARY_ID, faceLibraryId);
        List<Face> faceList = faceService.list(queryWrapper2);
        List<FaceVO> resp = new ArrayList<>();
        if (faceList != null && faceList.size() > 0) {

            for (Face face : faceList
            ) {
                FaceVO newFaceVO = new FaceVO();
                newFaceVO.setId(face.getId());
                newFaceVO.setPersonName(face.getPersonName());
                newFaceVO.setImageUrl(face.getImageUrl());
                newFaceVO.setFaceFeatures(BaseUtils.blob2faceFeature(face.getFeatures()));
                resp.add(newFaceVO);
            }
        }
        return resp;
    }

    @Override
    public boolean checkFaceLibraryExist(int libraryId) {
        QueryWrapper<FaceLibrary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(FaceLibrary.COL_ID, libraryId);
        FaceLibrary existRecord = getOne(queryWrapper, false);
        if (existRecord == null) {
            return false;
        }
        return true;
    }
}





