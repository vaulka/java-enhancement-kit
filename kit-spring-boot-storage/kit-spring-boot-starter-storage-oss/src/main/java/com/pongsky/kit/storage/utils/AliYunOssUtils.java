package com.pongsky.kit.storage.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
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
     * secretAccessKey
     */
    private final String secretAccessKey;

    /**
     * OSS client
     */
    private final OSS client;

    public AliYunOssUtils(String endpoint, String bucket, String accessKeyId, String secretAccessKey) {
        this.endpoint = endpoint;
        this.bucket = bucket;
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.client = getClient();
        this.createBucket();
    }

    /**
     * 创建 client
     *
     * @return 创建 client
     * @author pengsenhao
     */
    private OSS getClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, secretAccessKey);
    }

    /***
     *  创建 bucket（如果不存在，则自动创建）
     *
     * @author pengsenhao
     */
    @Override
    public void createBucket() {
        // 判断 bucket 是否存在
        boolean isExists;
        try {
            isExists = client.doesBucketExist(bucket);
        } catch (OSSException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        } catch (ClientException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        }
        if (isExists) {
            return;
        }
        // 不存在则创建
        try {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket)
                    // 设置 bucket 策略为公共读
                    .withCannedACL(CannedAccessControlList.PublicRead);
            client.createBucket(createBucketRequest);
        } catch (OSSException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        } catch (ClientException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param fileName    文件名称
     * @param contentType 文件类型
     * @param inputStream input 流
     * @return 文件访问路径
     * @author pengsenhao
     */
    @Override
    public String upload(String fileName, String contentType, InputStream inputStream) {
        try (inputStream) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, inputStream);
            client.putObject(putObjectRequest);
        } catch (OSSException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        } catch (ClientException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
        return "/" + fileName;
    }

}
