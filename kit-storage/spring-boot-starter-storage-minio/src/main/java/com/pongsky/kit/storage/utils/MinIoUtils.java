package com.pongsky.kit.storage.utils;

import com.google.common.collect.HashMultimap;
import com.pongsky.kit.storage.exception.MinIoException;
import io.minio.BucketExistsArgs;
import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.SetBucketPolicyArgs;
import io.minio.UploadPartResponse;
import io.minio.messages.Part;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * MinIO 工具类
 *
 * @author pengsenhao
 */
public class MinIoUtils {

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
    private final CustomMinioClient client;

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
    private CustomMinioClient getClient() {
        return new CustomMinioClient(MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build());
    }

    /**
     * 默认 Content-Type
     */
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

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
    public void createBucket() {
        // 判断 bucket 是否存在
        boolean isExists;
        try {
            isExists = client.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build());
        } catch (Exception e) {
            throw new MinIoException(e.getLocalizedMessage(), e);
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
            throw new MinIoException(e.getLocalizedMessage(), e);
        }
        // 设置 bucket 策略为公共读
        try {
            client.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(bucket)
                    .config(BUCKET_POLICY_BY_PUBLIC_READ.replace(BUCKET_PLACEHOLDER, bucket))
                    .build());
        } catch (Exception e) {
            throw new MinIoException(e.getLocalizedMessage(), e);
        }
    }

    public String upload(String fileName, InputStream inputStream) {
        return this.upload(fileName, DEFAULT_CONTENT_TYPE, inputStream);
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
    public String upload(String fileName, String contentType, InputStream inputStream) {
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            throw new MinIoException(e.getLocalizedMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "/" + fileName;
    }

    public String initPartUpload(String fileName) {
        return this.initPartUpload(fileName, DEFAULT_CONTENT_TYPE);
    }

    /**
     * 获取分片上传事件ID
     *
     * @param fileName    文件名称
     * @param contentType 内容类型
     * @return 分片上传事件ID
     */
    public String initPartUpload(String fileName, String contentType) {
        HashMultimap<String, String> headers = HashMultimap.create();
        headers.put("Content-Type", contentType);
        CreateMultipartUploadResponse response;
        try {
            response = client.initPartUpload(bucket, null, fileName, headers, null);
        } catch (Exception e) {
            throw new MinIoException(e.getLocalizedMessage(), e);
        }
        return response.result().uploadId();
    }

    public Part partUpload(String uploadId, int partNumber, int partSize, String fileName, InputStream inputStream) {
        return this.partUpload(uploadId, partNumber, partSize, fileName, new BufferedInputStream(inputStream));
    }

    /**
     * 分片上传
     * <p>
     * 需要注意，最小分片大小需要 5MB，不然合并会报错，相关 <a href="https://github.com/minio/minio/issues/11076">issues</a>
     * <p>
     * 也就意味着，适用场景为文件 >= 10MB，使用分片上传更合适。（10 MB 以下也只能分一片，不如用简单上传，减少接口请求次数）
     *
     * @param uploadId    分片上传事件ID
     * @param partNumber  当前分片数
     * @param partSize    当前分片文件大小，单位为字节，譬如：1MB = 1 * 1024 * 1024L
     * @param fileName    文件名称
     * @param inputStream input 流
     * @return 分片信息
     */
    public Part partUpload(String uploadId, int partNumber, int partSize, String fileName, BufferedInputStream inputStream) {
        UploadPartResponse response;
        try {
            response = client.partUpload(bucket, null, fileName, inputStream, partSize, uploadId, partNumber, null, null);
        } catch (Exception e) {
            throw new MinIoException(e.getLocalizedMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Part(response.partNumber(), response.etag());
    }

    public String completePartUpload(String uploadId, String fileName, List<Part> parts) {
        return this.completePartUpload(uploadId, fileName, parts.toArray(new Part[0]));
    }

    /**
     * 查询分片信息
     *
     * @param uploadId 分片上传事件ID
     * @param fileName 文件名称
     * @return 分片信息列表
     */
    public List<Part> listPart(String uploadId, String fileName) {
        ListPartsResponse response;
        try {
            response = client.listPart(bucket, null, fileName, null, null, uploadId,
                    null, null);
        } catch (Exception e) {
            throw new MinIoException(e.getLocalizedMessage(), e);
        }
        return response.result().partList();
    }

    /**
     * 合并分片上传
     *
     * @param uploadId 分片上传事件ID
     * @param fileName 文件名称
     * @param parts    分片信息列表
     * @return 文件访问路径
     */
    public String completePartUpload(String uploadId, String fileName, Part[] parts) {
        try {
            client.completePartUpload(bucket, null, fileName, uploadId, parts,
                    null, null);
        } catch (Exception e) {
            throw new MinIoException(e.getLocalizedMessage(), e);
        }
        return "/" + fileName;
    }

}
