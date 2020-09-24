package com.hankutech.ai.face.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hankutech.ai.face.service.db.FaceMapper;
import com.hankutech.ai.face.service.db.Face;
import com.hankutech.ai.face.service.FaceService;

@Service
public class FaceServiceImpl extends ServiceImpl<FaceMapper, Face> implements FaceService {

}




