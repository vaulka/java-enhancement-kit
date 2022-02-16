package com.pongsky.springcloud.controller.console;

import com.pongsky.springcloud.config.StorageConfig;
import com.pongsky.springcloud.config.SystemConfig;
import com.pongsky.springcloud.exception.ValidationException;
import com.pongsky.springcloud.response.annotation.ResponseResult;
import com.pongsky.springcloud.utils.storage.StorageUtils;
import com.pongsky.springcloud.utils.storage.UploadFileInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 公共组件 by 云存储
 *
 * @author pengsenhao
 */
@Api(tags = "公共组件 by 云存储")
@Slf4j
@ResponseResult
@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(value = "storage.enable", havingValue = "true")
@RequestMapping(value = "/console/${application.module}/common/storage")
public class ConsoleStorageController {

    private final StorageConfig storageConfig;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件访问路径
     * @throws IOException IOException
     * @author pengsenhao
     */
    @ApiOperation("文件上传")
    @PostMapping
    public UploadFileInfo upload(@RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty() || file.getSize() == 0 || StringUtils.isBlank(file.getOriginalFilename())) {
            throw new ValidationException("请选择文件进行上传");
        }
        StorageUtils storageUtils = storageConfig.getUtils();
        if (storageUtils == null) {
            return null;
        }
        String fileName = storageUtils.buildFileName(file.getOriginalFilename(),
                SystemConfig.getApplicationName(), SystemConfig.getActive().name());
        String url = storageUtils.upload(fileName, file.getInputStream());
        return new UploadFileInfo(url);
    }

}
