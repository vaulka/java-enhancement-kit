package com.pongsky.springcloud.utils.storage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文件上传返回信息
 *
 * @author pengsenhao
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
