package com.hankutech.ai.face.controller;

import com.ar.face.faceenginesdk.struct.model.FaceDataInfo;
import com.ar.face.faceenginesdk.struct.model.FaceDetectInfo;
import com.ar.face.faceenginesdk.struct.model.FaceRecogInfo;
import com.ar.face.faceenginesdk.struct.model.detect.FaceDetectOutParam;
import com.ar.face.faceenginesdk.struct.model.recognition.FaceRecognitionOutParam;
import com.hankutech.ai.face.constant.Common;
import com.hankutech.ai.face.constant.RuntimeContext;
import com.hankutech.ai.face.pojo.request.FaceDetectParams;
import com.hankutech.ai.face.pojo.request.FaceRecognizeParams;
import com.hankutech.ai.face.pojo.response.BaseResponse;
import com.hankutech.ai.face.pojo.vo.FaceDetectResultVO;
import com.hankutech.ai.face.pojo.vo.FaceRecognizeResultVO;
import com.hankutech.ai.face.pojo.vo.face.*;
import com.hankutech.ai.face.service.FaceLibraryService;
import com.hankutech.ai.face.service.FaceService;
import com.hankutech.ai.face.service.db.Face;
import com.hankutech.ai.face.support.face.FaceEngineWrapper;
import com.hankutech.ai.face.worker.FaceDetectWorker;
import com.hankutech.ai.face.worker.FaceRecognizeWorker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.InvalidObjectException;
import java.util.ArrayList;

@Validated
@Tag(name = "/face", description = "人脸库接口")
@Slf4j
@RequestMapping(path = "/runtime/v1/image-classify/face")
@RestController

public class FaceController {

    private final FaceService faceService;
    private final FaceLibraryService faceLibraryService;

    @Autowired
    public FaceController(FaceLibraryService faceLibraryService, FaceService faceService, FaceLibraryService faceLibraryService1) {
        this.faceService = faceService;
        this.faceLibraryService = faceLibraryService1;
    }

    @Operation(summary = "上传一张图像进行人脸检测")
    @PostMapping(path = "/detect")
    public BaseResponse<FaceDetectResultVO> detect(@RequestBody @Validated FaceDetectParams request, @RequestParam Integer productId, @RequestParam String apikey) {
        BaseResponse<FaceDetectResultVO> resp = new BaseResponse<>();

        FaceDetectOutParam result = FaceDetectWorker.detect(request.getImageDataBase64());
        if (result.ar_error_code == 0 && result.faceDetectInfoArray != null && result.faceDetectInfoArray.length > 0) {
            FaceDetectResultVO vo = new FaceDetectResultVO();
            resp.setData(vo);
            vo.setFaceNum(result.faceDetectInfoArray.length);
            vo.setFaceInfoList(new ArrayList<>(vo.getFaceNum()));
            for (FaceDetectInfo fd :
                    result.faceDetectInfoArray) {


                FaceInfo newFaceInfo = new FaceInfo();
                newFaceInfo.setFaceRect(copyFaceRect(fd.faceDataInfo));
                newFaceInfo.setLandmarks(copyLandmarkst(fd.faceDataInfo));
                vo.getFaceInfoList().add(newFaceInfo);
            }
        }

        return resp;
    }

    @Operation(summary = "上传一张图像进行人脸识别")
    @PostMapping(path = "/recognize")
    public BaseResponse<FaceRecognizeResultVO> recognize(@RequestBody @Validated FaceRecognizeParams request, @RequestParam Integer productId, @RequestParam String apikey) {
        BaseResponse<FaceRecognizeResultVO> resp = new BaseResponse<>();

        boolean faceLibraryExist = faceLibraryService.checkFaceLibraryExist(request.getLibraryId());
        if (faceLibraryExist == false) {
            resp.fail("指定的人脸图库不存在");
            return resp;
        }

        FaceRecognitionOutParam result = FaceRecognizeWorker.recognize(request.getLibraryId(), request.getImageDataBase64());

        if (result.ar_error_code == 0 && result.faceRecognitionInfoArray != null && result.faceRecognitionInfoArray.length > 0) {
            FaceRecognizeResultVO vo = new FaceRecognizeResultVO();
            resp.setData(vo);
            vo.setFaceNum(result.faceRecognitionInfoArray.length);
            vo.setFaceInfoList(new ArrayList<>(vo.getFaceNum()));
            for (FaceRecogInfo fd :
                    result.faceRecognitionInfoArray) {

                FaceInfo newFaceInfo = new FaceInfo();
                newFaceInfo.setPersonName(Common.PERSON_NAME_NONE);
                if (fd.simiScore > Common.MIN_SIMI_SCORE) {
                    Face findFace = faceService.getById(fd.personId);
                    if (findFace != null) {
                        newFaceInfo.setPersonName(findFace.getPersonName());
                    }
                }

                newFaceInfo.setSimilarityScore(fd.simiScore);

                newFaceInfo.setFaceRect(copyFaceRect(fd.faceDataInfo));

                newFaceInfo.setLandmarks(copyLandmarkst(fd.faceDataInfo));

                vo.getFaceInfoList().add(newFaceInfo);
            }
        }
        return resp;
    }

    private Landmarks copyLandmarkst(FaceDataInfo faceDataInfo) {
        Landmarks landmarks = new Landmarks();
        landmarks.setLeftEye(new LeftEye(faceDataInfo.landmarks[0].x, faceDataInfo.landmarks[0].y));
        landmarks.setRightEye(new RightEye(faceDataInfo.landmarks[1].x, faceDataInfo.landmarks[1].y));
        landmarks.setNose(new Nose(faceDataInfo.landmarks[2].x, faceDataInfo.landmarks[2].y));
        landmarks.setLeftMouthCorner(new LeftMouthCorner(faceDataInfo.landmarks[3].x, faceDataInfo.landmarks[3].y));
        landmarks.setRightMouthCorner(new RightMouthCorner(faceDataInfo.landmarks[4].x, faceDataInfo.landmarks[4].y));
        return landmarks;
    }

    private FaceRect copyFaceRect(FaceDataInfo faceDataInfo) {
        FaceRect faceRect = new FaceRect();
        faceRect.setXmax(faceDataInfo.faceRc.xmax);
        faceRect.setXmin(faceDataInfo.faceRc.xmin);
        faceRect.setYmax(faceDataInfo.faceRc.ymax);
        faceRect.setYmin(faceDataInfo.faceRc.ymin);
        return faceRect;
    }


    @Operation(summary = "重新加载人脸库")
    @GetMapping(path = "/reload")
    public BaseResponse reload(@RequestParam Integer requestFaceLibraryId, @RequestParam Integer productId, @RequestParam String apikey) {
        System.out.println("重新加载人脸库" + requestFaceLibraryId);
        BaseResponse resp = new BaseResponse<>();
        if (requestFaceLibraryId > 0) {
            RuntimeContext.CurrentFaceLibraryId = requestFaceLibraryId;
        }
        RuntimeContext.PersonFaceVOList = faceLibraryService.getFaceList(RuntimeContext.CurrentFaceLibraryId);
        try {
            FaceEngineWrapper.initNativeSDK(RuntimeContext.PersonFaceVOList);
            resp.success("reload success");
        } catch (InvalidObjectException e) {
            e.printStackTrace();
            resp.fail(e.getMessage());
        }

        return resp;
    }

}
