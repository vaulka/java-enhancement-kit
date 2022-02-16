package com.pongsky.springcloud.utils.storage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-02-13 9:33 下午
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UploadFileInfo {

    public UploadFileInfo(String url) {
        this.url = url;
        this.fullUrl = url;
    }

    /**
     * 文件 uri
     */
    private String url;

    /**
     * 文件全 uri
     */
    @StorageResourceMark
    private String fullUrl;

}
