package com.pongsky.springcloud.utils.storage;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import java.io.InputStream;

/**
 * MinIO 工具类
 *
 * @author pengsenhao
 */
public class MinIoUtils implements StorageUtils {

    /**
     * endpoint
     */
    private final String endpoint;

    /**
     * bucket
     */
    private final String bucket;

    /**
     * accessKey
     */
    private final String accessKey;

    /**
     * secretKey
     */
    private final String secretKey;

    public MinIoUtils(String endpoint, String bucket, String accessKey, String secretKey) {
        this.endpoint = endpoint;
        this.bucket = bucket;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 创建 client
     *
     * @return 创建 client
     * @author pengsenhao
     */
    private MinioClient getClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
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
        MinioClient client = this.getClient();
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
        return "/" + fileName;
    }

}

