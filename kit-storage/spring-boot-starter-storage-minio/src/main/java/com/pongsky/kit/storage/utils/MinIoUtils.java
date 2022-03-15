package com.pongsky.kit.storage.utils;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.SetBucketPolicyArgs;

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

    /**
     * MinIO client
     */
    private final MinioClient client;

    public MinIoUtils(String endpoint, String bucket, String accessKey, String secretKey) {
        this.endpoint = endpoint;
        this.bucket = bucket;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        client = this.getClient();
        this.createBucket();
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
     * bucket 占位符
     */
    private static final String BUCKET_PLACEHOLDER = "{0}";

    /**
     * 公共读策略
     *
     * @see <a href="https://docs.min.io/docs/java-client-api-reference.html#setBucketPolicy">setBucketPolicy</a>
     */
    private static final String BUCKET_POLICY_BY_PUBLIC_READ = "{\n" +
            "    \"Version\": \"2012-10-17\",\n" +
            "    \"Statement\": [\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": \"*\",\n" +
            "            \"Action\": [\n" +
            "                \"s3:GetBucketLocation\",\n" +
            "                \"s3:ListBucket\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::" + BUCKET_PLACEHOLDER + "\"\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": \"*\",\n" +
            "            \"Action\": [\n" +
            "                \"s3:GetObject\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::" + BUCKET_PLACEHOLDER + "/*\"\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";

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
            isExists = client.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
        if (isExists) {
            return;
        }
        // 不存在则创建
        try {
            client.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
        // 设置 bucket 策略为公共读
        try {
            client.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(bucket)
                    .config(BUCKET_POLICY_BY_PUBLIC_READ.replace(BUCKET_PLACEHOLDER, bucket))
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
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
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
        return "/" + fileName;
    }

}
