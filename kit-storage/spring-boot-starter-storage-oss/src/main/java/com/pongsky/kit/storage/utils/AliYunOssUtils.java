package com.pongsky.kit.storage.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.UploadPartRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 阿里云 OSS 工具类
 *
 * @author pengsenhao
 */
public class AliYunOssUtils implements StorageUtils<PartETag> {

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
     */
    private OSS getClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, secretAccessKey);
    }

    /**
     * 创建 bucket（如果不存在，则自动创建）
     * <p>
     * docs：
     * <ui>
     * <li><a href="https://help.aliyun.com/document_detail/32012.html">创建存储空间</a></li>
     * </ui>
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
        CreateBucketRequest request = new CreateBucketRequest(bucket)
                // 设置 bucket 策略为公共读
                .withCannedACL(CannedAccessControlList.PublicRead);
        try {
            client.createBucket(request);
        } catch (OSSException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        } catch (ClientException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        }
    }

    /**
     * 文件上传
     * docs：
     * <ui>
     * <li><a href="https://help.aliyun.com/document_detail/84781.html">简单上传</a></li>
     * </ui>
     *
     * @param fileName    文件名称
     * @param contentType 文件类型
     * @param inputStream input 流
     * @return 文件访问路径
     */
    @Override
    public String upload(String fileName, String contentType, InputStream inputStream) {
        PutObjectRequest request = new PutObjectRequest(bucket, fileName, inputStream);
        try {
            client.putObject(request);
        } catch (OSSException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        } catch (ClientException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
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

    /**
     * 获取分片上传ID
     * docs：
     * <ui>
     * <li><a href="https://help.aliyun.com/document_detail/84786.html">分片上传</a></li>
     * </ui>
     *
     * @param fileName 文件名称
     * @return 分片上传ID
     */
    @Override
    public String initPartUpload(String fileName) {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucket, fileName);
        try {
            return client.initiateMultipartUpload(request).getUploadId();
        } catch (OSSException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        } catch (ClientException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        }
    }

    /**
     * 分片上传
     * docs：
     * <ui>
     * <li><a href="https://help.aliyun.com/document_detail/84786.html">分片上传</a></li>
     * </ui>
     *
     * @param uploadId    分片上传事件ID
     * @param partNumber  当前分片数
     * @param partSize    当前分片文件大小，单位为字节，譬如：1MB = 1 * 1024 * 1024L
     * @param fileName    文件名称
     * @param inputStream input 流
     * @return 分片ID
     */
    @Override
    public PartETag partUpload(String uploadId, Integer partNumber, Long partSize, String fileName, InputStream inputStream) {
        UploadPartRequest uploadPartRequest = new UploadPartRequest();
        uploadPartRequest.setBucketName(bucket);
        uploadPartRequest.setKey(fileName);
        uploadPartRequest.setUploadId(uploadId);
        uploadPartRequest.setPartSize(partSize);
        uploadPartRequest.setInputStream(inputStream);
        uploadPartRequest.setPartNumber(partNumber);
        try {
            return client.uploadPart(uploadPartRequest).getPartETag();
        } catch (OSSException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        } catch (ClientException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 合并分片上传
     * docs：
     * <ui>
     * <li><a href="https://help.aliyun.com/document_detail/84786.html">分片上传</a></li>
     * </ui>
     *
     * @param uploadId 分片上传事件ID
     * @param fileName 文件名称
     * @param parts    分片信息列表
     * @return 文件访问路径
     */
    @Override
    public String completePartUpload(String uploadId, String fileName, List<PartETag> parts) {
        CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(bucket, fileName, uploadId, parts);
        try {
            client.completeMultipartUpload(request);
        } catch (OSSException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        } catch (ClientException e) {
            throw new RuntimeException(e.getErrorMessage(), e);
        }
        return "/" + fileName;
    }

}
