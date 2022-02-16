package com.pongsky.springcloud.utils.storage;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.InputStream;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-02-13 6:34 下午
 */
public class AliYunOssUtils implements StorageUtils {

    /**
     * endpoint
     */
    private final String endpoint;

    /**
     * bucket
     */
    private final String bucket;

    /**
     * accessKeyId
     */
    private final String accessKeyId;

    /**
     * accessKeySecret
     */
    private final String accessKeySecret;

    public AliYunOssUtils(String endpoint, String bucket, String accessKeyId, String accessKeySecret) {
        this.endpoint = endpoint;
        this.bucket = bucket;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    /**
     * 文件上传
     *
     * @param fileName    文件名称
     * @param inputStream input 流
     * @return 文件访问路径
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-02-13 6:37 下午
     */
    @Override
    public String upload(String fileName, InputStream inputStream) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, inputStream);
            ossClient.putObject(putObjectRequest);
        } catch (OSSException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        } catch (ClientException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return "/" + fileName;
    }

}
