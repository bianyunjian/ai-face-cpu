package com.hankutech.ai.face.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hankutech.ai.face.pojo.request.FaceImageAddParams;
import com.hankutech.ai.face.pojo.request.FaceImageDeleteParams;
import com.hankutech.ai.face.pojo.request.FaceImageUpdateParams;
import com.hankutech.ai.face.pojo.response.BaseResponse;
import com.hankutech.ai.face.pojo.vo.FaceImageListVO;
import com.hankutech.ai.face.pojo.vo.FaceImageVO;
import com.hankutech.ai.face.service.FaceLibraryService;
import com.hankutech.ai.face.service.FaceService;
import com.hankutech.ai.face.service.db.Face;
import com.hankutech.ai.face.worker.FaceRegisterWorker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Validated
@Tag(name = "/face/image", description = "人脸库接口")
@Slf4j
@RequestMapping(path = "/runtime/v1/image-classify/face/library/image")
@RestController

public class FaceImageController {

    private final FaceService faceService;
    private final FaceLibraryService faceLibraryService;

    @Autowired
    public FaceImageController(FaceLibraryService faceLibraryService, FaceService faceService, FaceLibraryService faceLibraryService1) {

        this.faceService = faceService;
        this.faceLibraryService = faceLibraryService1;
    }

    @Operation(summary = "新增人脸数据")
    @PostMapping(path = "/add")
    public BaseResponse<FaceImageVO> add(@RequestBody @Validated FaceImageAddParams request, @RequestParam Integer productId, @RequestParam String apikey) {
        BaseResponse<FaceImageVO> resp = new BaseResponse<>();

        boolean faceLibraryExist = faceLibraryService.checkFaceLibraryExist(request.getLibraryId());
        if (faceLibraryExist == false) {
            resp.fail("指定的人脸图库不存在");
            return resp;
        }

        boolean facePersonNameExist = faceService.checkPersonNameExist(request.getPersonName(), null);
        if (facePersonNameExist) {
            resp.fail("已经存在相同名称的人脸数据");
            return resp;
        }

        byte[] faceFeatures = FaceRegisterWorker.getFaceFeatures(request.getImageDataBase64());
        if (faceFeatures == null) {
            resp.fail("无法检测到有效的人脸数据");
            return resp;
        }
        Face newFace = new Face();
        newFace.setPersonName(request.getPersonName());
        newFace.setFaceLibraryId(request.getLibraryId());

        newFace.setFeatures(faceFeatures);
        newFace.setImageUrl(FaceRegisterWorker.saveFaceImage(request.getImageDataBase64(), request.getLibraryId(), request.getPersonName()));
        boolean saveSuccess = faceService.save(newFace);
        if (saveSuccess) {
            FaceImageVO vo = new FaceImageVO();
            vo.setImageId(newFace.getId());
            resp.success("新增人脸数据成功");
            resp.setData(vo);
        } else {
            resp.fail("新增人脸数据失败");
        }
        return resp;
    }


    @Operation(summary = "更新人脸数据")
    @PostMapping(path = "/update")
    public BaseResponse update(@RequestBody @Validated FaceImageUpdateParams request, @RequestParam Integer productId, @RequestParam String apikey) {
        BaseResponse resp = new BaseResponse<>();

        boolean faceLibraryExist = faceLibraryService.checkFaceLibraryExist(request.getLibraryId());
        if (faceLibraryExist == false) {
            resp.fail("指定的人脸图库不存在");
            return resp;
        }

        List<String> excludeImageIds = new ArrayList<>();
        excludeImageIds.add(request.getImageId());
        boolean facePersonNameExist = faceService.checkPersonNameExist(request.getPersonName(), excludeImageIds);
        if (facePersonNameExist) {
            resp.fail("已经存在相同名称的人脸数据");
            return resp;
        }

        byte[] faceFeatures = FaceRegisterWorker.getFaceFeatures(request.getImageDataBase64());
        if (faceFeatures == null) {
            resp.fail("无法检测到有效的人脸数据");
            return resp;
        }
        Face newFace = new Face();
        newFace.setId(Integer.parseInt(request.getImageId()));
        newFace.setPersonName(request.getPersonName());
        newFace.setFaceLibraryId(request.getLibraryId());
        newFace.setFeatures(faceFeatures);
        newFace.setImageUrl(FaceRegisterWorker.saveFaceImage(request.getImageDataBase64(), request.getLibraryId(), request.getPersonName()));
        boolean updateSuccess = faceService.updateById(newFace);
        if (updateSuccess) {
            resp.success("更新人脸数据成功");
        } else {
            resp.fail("更新人脸数据失败");
        }
        return resp;
    }

    @Operation(summary = "删除人脸数据")
    @PostMapping(path = "/delete")
    public BaseResponse delete(@RequestBody @Validated FaceImageDeleteParams request, @RequestParam Integer productId, @RequestParam String apikey) {
        BaseResponse resp = new BaseResponse<>();

        boolean faceLibraryExist = faceLibraryService.checkFaceLibraryExist(request.getLibraryId());
        if (faceLibraryExist == false) {
            resp.fail("指定的人脸图库不存在");
            return resp;
        }
        QueryWrapper<Face> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Face.COL_ID, request.getImageId());
        boolean deleteSuccess = faceService.remove(queryWrapper);
        if (deleteSuccess) {
            resp.success("删除人脸数据成功");

        } else {
            resp.fail("删除人脸数据失败");
        }
        return resp;
    }


    @Operation(summary = "查询图库中人脸列表")
    @GetMapping(path = "/list")
    public BaseResponse list(@RequestParam Integer libraryId, @RequestParam Integer productId, @RequestParam String apikey) {
        BaseResponse<FaceImageListVO> resp = new BaseResponse<>();


        boolean faceLibraryExist = faceLibraryService.checkFaceLibraryExist(libraryId);
        if (faceLibraryExist == false) {
            resp.fail("指定的人脸图库不存在");
            return resp;
        }

        FaceImageListVO listData = new FaceImageListVO();
        resp.setData(listData);
        QueryWrapper<Face> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Face.COL_FACE_LIBRARY_ID, libraryId);
        List<Face> existRecords = faceService.list(queryWrapper);
        if (existRecords != null) {
            listData.setTotalCount(existRecords.size());
            listData.setLibraryList(new ArrayList<>(listData.getTotalCount()));
            for (Face fl : existRecords
            ) {
                FaceImageVO data = new FaceImageVO();
                data.setImageId(fl.getId());
                data.setPersonName(fl.getPersonName());
                data.setImageUrl(fl.getImageUrl());
                listData.getLibraryList().add(data);
            }
        }

        resp.success("查询图库中人脸列表成功");
        return resp;
    }

    @Operation(summary = "查询图库中人脸信息详情")
    @GetMapping(path = "/info")
    public BaseResponse info(@RequestParam @Validated int libraryId, @RequestParam @Validated int imageId, @RequestParam Integer productId, @RequestParam String apikey) {
        BaseResponse<FaceImageVO> resp = new BaseResponse<>();

        QueryWrapper<Face> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Face.COL_ID, imageId);
        queryWrapper.eq(Face.COL_FACE_LIBRARY_ID, libraryId);
        Face face = faceService.getOne(queryWrapper, false);
        if (face == null) {
            resp.fail("不存在指定的人脸信息");
            return resp;
        }

        FaceImageVO data = new FaceImageVO();
        data.setImageId(face.getId());
        data.setPersonName(face.getPersonName());
        data.setImageUrl(face.getImageUrl());
        resp.setData(data);
        resp.success("查询图库中人脸信息详情成功");
        return resp;
    }


}
