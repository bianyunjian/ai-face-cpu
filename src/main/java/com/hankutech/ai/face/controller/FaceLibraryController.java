package com.hankutech.ai.face.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hankutech.ai.face.pojo.request.LibraryCreateParams;
import com.hankutech.ai.face.pojo.request.LibraryDeleteParams;
import com.hankutech.ai.face.pojo.request.LibraryUpdateParams;
import com.hankutech.ai.face.pojo.response.BaseResponse;
import com.hankutech.ai.face.pojo.vo.FaceLibraryListVO;
import com.hankutech.ai.face.pojo.vo.FaceLibraryVO;
import com.hankutech.ai.face.service.FaceLibraryService;
import com.hankutech.ai.face.service.db.FaceLibrary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Validated
@Tag(name = "/face", description = "人脸库接口")
@Slf4j
@RequestMapping(path = "/runtime/v1/image-classify/face/library")
@RestController

public class FaceLibraryController {

    private final FaceLibraryService faceLibraryService;

    @Autowired
    public FaceLibraryController(FaceLibraryService faceLibraryService) {
        this.faceLibraryService = faceLibraryService;
    }

    @Operation(summary = "创建人脸图库")
    @PostMapping(path = "/create")
    public BaseResponse<FaceLibraryVO> create(@RequestBody @Validated LibraryCreateParams request, @RequestParam Integer productId, @RequestParam String apikey) {
        BaseResponse<FaceLibraryVO> resp = new BaseResponse<>();

        QueryWrapper<FaceLibrary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(FaceLibrary.COL_NAME, request.getLibraryName());
        FaceLibrary existRecord = faceLibraryService.getOne(queryWrapper, false);
        if (existRecord != null) {
            resp.fail("创建人脸图库失败,已经存在相同的图库名称");
            return resp;
        }
        FaceLibrary newFaceLibrary = new FaceLibrary();
        newFaceLibrary.setName(request.getLibraryName());
        newFaceLibrary.setProductId(productId);
        newFaceLibrary.setDescription(request.getDescription());
        boolean saveSuccess = faceLibraryService.save(newFaceLibrary);
        if (saveSuccess) {
            FaceLibraryVO vo = new FaceLibraryVO();
            vo.setLibraryId(newFaceLibrary.getId());
            resp.success("创建人脸图库成功");
            resp.setData(vo);
        } else {
            resp.fail("创建人脸图库失败");
        }
        return resp;
    }


    @Operation(summary = "更新图库")
    @PostMapping(path = "/update")
    public BaseResponse update(@RequestBody @Validated LibraryUpdateParams request, @RequestParam Integer productId, @RequestParam String apikey) {
        BaseResponse resp = new BaseResponse<>();

        QueryWrapper<FaceLibrary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(FaceLibrary.COL_NAME, request.getLibraryName());
        FaceLibrary existRecord = faceLibraryService.getOne(queryWrapper, false);
        if (existRecord != null && existRecord.getId() != request.getLibraryId()) {
            resp.fail("更新人脸图库失败,已经存在相同的图库名称");
            return resp;
        }

        queryWrapper.clear();
        queryWrapper.eq(FaceLibrary.COL_ID, request.getLibraryId());
        existRecord = faceLibraryService.getOne(queryWrapper, false);
        if (existRecord == null) {
            resp.fail("不存在指定的人脸图库");
            return resp;
        }


        existRecord.setName(request.getLibraryName());
        existRecord.setDescription(request.getDescription());
        boolean updateSuccess = faceLibraryService.updateById(existRecord);
        if (updateSuccess) {
            resp.success("更新人脸图库成功");

        } else {
            resp.fail("更新人脸图库失败");
        }
        return resp;
    }

    @Operation(summary = "删除图库")
    @PostMapping(path = "/delete")
    public BaseResponse delete(@RequestBody @Validated LibraryDeleteParams request, @RequestParam Integer productId, @RequestParam String apikey) {
        BaseResponse resp = new BaseResponse<>();

        QueryWrapper<FaceLibrary> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(FaceLibrary.COL_ID, request.getLibraryId());
        FaceLibrary existRecord = faceLibraryService.getOne(queryWrapper, false);
        if (existRecord == null) {
            resp.fail("不存在指定的人脸图库");
            return resp;
        }

        boolean deleteSuccess = faceLibraryService.remove(queryWrapper);
        if (deleteSuccess) {
            resp.success("删除人脸图库成功");

        } else {
            resp.fail("删除人脸图库失败");
        }
        return resp;
    }

    @Operation(summary = "查询图库详情")
    @GetMapping(path = "/info")
    public BaseResponse info(@RequestParam @Validated int libraryId, @RequestParam Integer productId, @RequestParam String apikey) {
        BaseResponse<FaceLibraryVO> resp = new BaseResponse<>();

        QueryWrapper<FaceLibrary> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(FaceLibrary.COL_ID, libraryId);
        FaceLibrary existRecord = faceLibraryService.getOne(queryWrapper, false);
        if (existRecord == null) {
            resp.fail("不存在指定的人脸图库");
            return resp;
        }

        FaceLibraryVO data = new FaceLibraryVO();
        data.setLibraryId(libraryId);
        data.setLibraryName(existRecord.getName());
        data.setDescription(existRecord.getDescription());
        resp.setData(data);
        resp.success("查询图库详情成功");
        return resp;
    }


    @Operation(summary = "查询图库列表")
    @GetMapping(path = "/list")
    public BaseResponse list(@RequestParam Integer productId, @RequestParam String apikey) {
        BaseResponse<FaceLibraryListVO> resp = new BaseResponse<>();
        FaceLibraryListVO listData = new FaceLibraryListVO();
        resp.setData(listData);
        List<FaceLibrary> existRecords = faceLibraryService.list();
        if (existRecords != null) {
            listData.setTotalCount(existRecords.size());
            listData.setLibraryList(new ArrayList<>(listData.getTotalCount()));
            for (FaceLibrary fl : existRecords
            ) {
                FaceLibraryVO data = new FaceLibraryVO();
                data.setLibraryId(fl.getId());
                data.setLibraryName(fl.getName());
                data.setDescription(fl.getDescription());
                listData.getLibraryList().add(data);
            }
        }

        resp.success("查询图库列表成功");
        return resp;
    }
//    @Operation(summary = "获取所有人脸")
//    @GetMapping(path = "/all")
//    public BaseResponse<PersonLibraryVO> getAll() {
//        BaseResponse<PersonLibraryVO> resp = new BaseResponse<>();
//        PersonLibraryVO data = _personService.getPersonLibrary();
//        resp.success("获取所有人脸成功", data);
//        return resp;
//    }

//    @Operation(summary = "创建人脸图库")
//    @PostMapping(path = "/create")
//    public BaseResponse<PagedData<PersonVO>> queryTable(@RequestBody @Validated QueryRequest<PersonParams> request) {
//        PagedData<PersonVO> data = _personService.queryPersonTable(request.getPagedParams(), request.getQueryParams());
//        BaseResponse<PagedData<PersonVO>> resp = new BaseResponse<>();
//        resp.success("分页查询人脸成功", data);
//        return resp;
//    }
}
