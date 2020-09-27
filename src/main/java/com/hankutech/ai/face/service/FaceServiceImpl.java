package com.hankutech.ai.face.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hankutech.ai.face.service.db.Face;
import com.hankutech.ai.face.service.db.FaceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaceServiceImpl extends ServiceImpl<FaceMapper, Face> implements FaceService {

    @Override
    public boolean checkPersonNameExist(String personName, List<String> excludeImageIds) {

        QueryWrapper<Face> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Face.COL_PERSON_NAME, personName);

        if (excludeImageIds != null && excludeImageIds.size() > 0) {
            queryWrapper.notIn(Face.COL_ID, excludeImageIds);
        }

        Face existRecord = getOne(queryWrapper, false);
        if (existRecord == null) {
            return false;
        }
        return true;
    }
}




