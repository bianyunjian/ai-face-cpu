package com.hankutech.ai.face;

import com.hankutech.ai.face.constant.Common;
import com.hankutech.ai.face.constant.RuntimeContext;
import com.hankutech.ai.face.service.FaceLibraryService;
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
        FaceEngineWrapper.initNativeSDK(RuntimeContext.PersonFaceVOList);


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
