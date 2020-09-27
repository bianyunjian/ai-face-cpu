package com.hankutech.ai.face;

import com.ar.face.faceenginesdk.struct.model.FaceRegInfo;
import com.ar.face.faceenginesdk.struct.model.register.FaceRegisterOutParam;
import com.hankutech.ai.face.constant.Common;
import com.hankutech.ai.face.constant.RuntimeContext;
import com.hankutech.ai.face.pojo.vo.FaceVO;
import com.hankutech.ai.face.service.FaceLibraryService;
import com.hankutech.ai.face.support.BaseUtils;
import com.hankutech.ai.face.support.face.FaceEngineConfigParam;
import com.hankutech.ai.face.support.face.FaceEngineWrapper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.List;

/**
 * 艾信智慧医疗-人脸识别服务
 *
 * @author ZhangXi
 */
@SpringBootApplication
public class AIFaceApplication implements ApplicationRunner, DisposableBean, WebMvcConfigurer {

    @Value("${app.face.saveRegisterImage}")
    private Boolean saveRegisterImage;

    @Value("${app.face.registerImageDir}")
    private String registerImageDir;

    @Value("${app.face.saveRecognizeImage}")
    private Boolean saveRecognizeImage;

    @Value("${app.face.recognizeImageDir}")
    private String recognizeImageDir;

    @Value("${app.face.defaultFaceLibraryId}")
    private int defaultFaceLibraryId;

    @Autowired
    private FaceLibraryService faceLibraryService;


    public static void main(String[] args) {

        System.out.println(Common.SERVICE_NAME + "开始启动");
        SpringApplication.run(AIFaceApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(Common.SERVICE_NAME + "执行一些启动后自定义的方法......");


        Common.saveRegisterImage = saveRegisterImage;
        Common.registerImageDir = registerImageDir;
        Common.saveRecognizeImage = saveRecognizeImage;
        Common.recognizeImageDir = recognizeImageDir;
        Common.init();

        //人脸库
        RuntimeContext.CurrentFaceLibraryId = defaultFaceLibraryId;
        RuntimeContext.PersonFaceVOList = faceLibraryService.getFaceList(RuntimeContext.CurrentFaceLibraryId);
        initNativeSDK(RuntimeContext.PersonFaceVOList);
        //启动线程


    }

    private void initNativeSDK(List<FaceVO> faceVOList) throws InvalidObjectException {
        FaceEngineConfigParam configParam = getFaceEngineConfigParam();
        FaceEngineWrapper.initFaceEngineWrapper(configParam);
        if (FaceEngineWrapper.getInstance().checkIfReady_Native() == false) {
            throw new InvalidObjectException("Native SDK初始化失败！");
        }

        if (faceVOList != null && faceVOList.size() > 0) {
            System.out.println("人脸库数量:" + faceVOList.size());
            HashMap<String, FaceRegisterOutParam> faceMap = new HashMap<>();

            for (FaceVO p : faceVOList) {
                FaceRegisterOutParam newParam = new FaceRegisterOutParam();
                newParam.id = p.getId();
                newParam.personName = p.getPersonName();
                newParam.faceRegInfoArray = new FaceRegInfo[1];
                newParam.faceRegInfoArray[0] = new FaceRegInfo();
                newParam.faceRegInfoArray[0].personId = String.valueOf(newParam.id);
                for (int i = 0; i < newParam.faceRegInfoArray[0].faceFtrArray.length; i++) {
                    newParam.faceRegInfoArray[0].faceFtrArray[i] = p.getFaceFeatures()[i];
                }
                faceMap.put(p.getId().toString(), newParam);
            }

            boolean initFaceSuccess = FaceEngineWrapper.getInstance().initRegisterFace_Native(faceMap);
            if (initFaceSuccess == false) {
                throw new InvalidObjectException("Native SDK初始化人脸库失败！");
            }
            System.out.println("Native SDK初始化人脸库成功！");
        }
    }


    public static FaceEngineConfigParam getFaceEngineConfigParam() {

        FaceEngineConfigParam configParam = new FaceEngineConfigParam();
        configParam.setConfigPath("");
        configParam.setSupportNativeFaceDetect(true);
        configParam.setSupportNativeFaceRecognize(true);

        String logFilePath = BaseUtils.getRootPath() + "/DebugLog";
        File f = new File(logFilePath);
        if (f.exists() == false) f.mkdirs();
        configParam.setLogFilePath(logFilePath);

        return configParam;
    }


    /**
     * 自定义静态资源访问
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }

    @Override
    public void destroy() throws Exception {
        System.out.println(Common.SERVICE_NAME + "结束");

    }

}
