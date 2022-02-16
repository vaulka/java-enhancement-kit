package com.pongsky.springcloud.utils.storage;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.InputStream;

/**
 * 阿里云 OSS 工具类
 *
 * @author pengsenhao
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
     * @author pengsenhao
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