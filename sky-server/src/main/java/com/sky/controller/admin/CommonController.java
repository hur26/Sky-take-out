package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil ;

    Tika tika = new Tika();

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
        try {
            //截取文件名
            String filename = file.getName();
            //截取文件后缀拓展名
            // 读取文件流，识别真实的 MIME 类型（比如 "image/png"、"text/plain"）
            String mimeType = tika.detect(file.getInputStream());
            // 将 MIME 类型转换成对应的后缀名（核心步骤）
            String extension = getSuffixFromMimeType(mimeType);
            //uuid 拼接新的文件名
            String newfilename = UUID.randomUUID().toString() + extension ;
            //文件请求路径
            String filepath = aliOssUtil.upload(file.getBytes(), newfilename);
            return Result.success(filepath) ;
        } catch (IOException e) {
            log.info("文件上传失败：{}",e);
        }
        return  Result.error("文件上传失败");
    }

    private String getSuffixFromMimeType(String mimeType) {
        Map<String, String> mimeToSuffix = new HashMap<>();
        // 图片类型
        mimeToSuffix.put("image/png", ".png");
        mimeToSuffix.put("image/jpeg", ".jpg");
        mimeToSuffix.put("image/jpg", ".jpg");
        mimeToSuffix.put("image/gif", ".gif");
        mimeToSuffix.put("image/webp", ".webp");
        // 文本类型
        mimeToSuffix.put("text/plain", ".txt");
        // 其他类型...
        return mimeToSuffix.getOrDefault(mimeType.toLowerCase(), ".unknown");
    }
}
