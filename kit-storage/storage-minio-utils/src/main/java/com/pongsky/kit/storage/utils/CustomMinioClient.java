package com.pongsky.kit.storage.utils;

import com.google.common.collect.Multimap;
import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.MinioClient;
import io.minio.S3Base;
import io.minio.UploadPartResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Part;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 自定义 MinIO Client
 * <p>
 * 分片上传相关方法都是 protected 修饰，更改为 public
 *
 * @author pengsenhao
 */
public class CustomMinioClient extends MinioClient {

    public CustomMinioClient(MinioClient client) {
        super(client);
    }

    /**
     * 将 {@link io.minio.S3Base#createMultipartUpload(String, String, String, com.google.common.collect.Multimap, com.google.common.collect.Multimap)} 修饰符改为 public
     *
     * @param bucketName       Name of the bucket.
     * @param region           Region name of buckets in S3 service.
     * @param objectName       Object name in the bucket.
     * @param headers          Request headers.
     * @param extraQueryParams Extra query parameters for request (Optional).
     * @return {@link CreateMultipartUploadResponse} object.
     * @throws ErrorResponseException    thrown to indicate S3 service returned an error response.
     * @throws InsufficientDataException thrown to indicate not enough data available in InputStream.
     * @throws InternalException         thrown to indicate internal library error.
     * @throws InvalidKeyException       thrown to indicate missing of HMAC SHA-256 library.
     * @throws InvalidResponseException  thrown to indicate S3 service returned invalid or no error response.
     * @throws IOException               thrown to indicate I/O error on S3 operation.
     * @throws NoSuchAlgorithmException  thrown to indicate missing of MD5 or SHA-256 digest library.
     * @throws XmlParserException        thrown to indicate XML parsing error.
     * @throws ServerException           ex
     */
    public CreateMultipartUploadResponse initPartUpload(String bucketName, String region, String objectName,
                                                        Multimap<String, String> headers, Multimap<String, String> extraQueryParams)
            throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException,
            ServerException, XmlParserException, ErrorResponseException, InternalException,
            InvalidResponseException {
        return this.createMultipartUpload(bucketName, region, objectName, headers, extraQueryParams);
    }

    /**
     * 将 {@link S3Base#uploadPart(String, String, String, Object, int, String, int, com.google.common.collect.Multimap, com.google.common.collect.Multimap)} 修饰符改为 public
     *
     * @param bucketName       Name of the bucket.
     * @param region           Region of the bucket (Optional).
     * @param objectName       Object name in the bucket.
     * @param data             Object data must be BufferedInputStream, RandomAccessFile, byte[] or String.
     * @param length           Length of object data.
     * @param uploadId         Upload ID.
     * @param partNumber       Part number.
     * @param extraHeaders     Extra headers for request (Optional).
     * @param extraQueryParams Extra query parameters for request (Optional).
     * @return String - Contains ETag.
     * @throws ErrorResponseException    thrown to indicate S3 service returned an error response.
     * @throws InsufficientDataException thrown to indicate not enough data available in InputStream.
     * @throws InternalException         thrown to indicate internal library error.
     * @throws InvalidKeyException       thrown to indicate missing of HMAC SHA-256 library.
     * @throws InvalidResponseException  thrown to indicate S3 service returned invalid or no error response.
     * @throws IOException               thrown to indicate I/O error on S3 operation.
     * @throws NoSuchAlgorithmException  thrown to indicate missing of MD5 or SHA-256 digest library.
     * @throws XmlParserException        thrown to indicate XML parsing error.
     * @throws ServerException           ex
     */
    public UploadPartResponse partUpload(
            String bucketName,
            String region,
            String objectName,
            Object data,
            int length,
            String uploadId,
            int partNumber,
            Multimap<String, String> extraHeaders,
            Multimap<String, String> extraQueryParams)
            throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException,
            ServerException, XmlParserException, ErrorResponseException, InternalException,
            InvalidResponseException {
        return this.uploadPart(bucketName,
                region,
                objectName,
                data,
                length,
                uploadId,
                partNumber,
                extraHeaders,
                extraQueryParams);
    }

    /**
     * 将 {@link S3Base#listParts(String, String, String, Integer, Integer, String, com.google.common.collect.Multimap, com.google.common.collect.Multimap)} 修饰符改为 public
     *
     * @param bucketName       Name of the bucket.
     * @param region           Name of the bucket (Optional).
     * @param objectName       Object name in the bucket.
     * @param maxParts         Maximum parts information to fetch (Optional).
     * @param partNumberMarker Part number marker (Optional).
     * @param uploadId         Upload ID.
     * @param extraHeaders     Extra headers for request (Optional).
     * @param extraQueryParams Extra query parameters for request (Optional).
     * @return {@link ListPartsResponse} object.
     * @throws ErrorResponseException    thrown to indicate S3 service returned an error response.
     * @throws InsufficientDataException thrown to indicate not enough data available in InputStream.
     * @throws InternalException         thrown to indicate internal library error.
     * @throws InvalidKeyException       thrown to indicate missing of HMAC SHA-256 library.
     * @throws InvalidResponseException  thrown to indicate S3 service returned invalid or no error response.
     * @throws IOException               thrown to indicate I/O error on S3 operation.
     * @throws NoSuchAlgorithmException  thrown to indicate missing of MD5 or SHA-256 digest library.
     * @throws XmlParserException        thrown to indicate XML parsing error.
     * @throws ServerException           ex
     */
    public ListPartsResponse listPart(
            String bucketName,
            String region,
            String objectName,
            Integer maxParts,
            Integer partNumberMarker,
            String uploadId,
            Multimap<String, String> extraHeaders,
            Multimap<String, String> extraQueryParams)
            throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException,
            ServerException, XmlParserException, ErrorResponseException, InternalException,
            InvalidResponseException {
        return this.listParts(bucketName, region, objectName, maxParts, partNumberMarker, uploadId, extraHeaders, extraQueryParams);
    }

    /**
     * 将 {@link S3Base#completeMultipartUpload(String, String, String, String, io.minio.messages.Part[], com.google.common.collect.Multimap, com.google.common.collect.Multimap)} 修饰符改为 public
     *
     * @param bucketName       Name of the bucket.
     * @param region           Region of the bucket.
     * @param objectName       Object name in the bucket.
     * @param uploadId         Upload ID.
     * @param parts            List of parts.
     * @param extraHeaders     Extra headers (Optional).
     * @param extraQueryParams Extra query parameters (Optional).
     * @throws ErrorResponseException    thrown to indicate S3 service returned an error response.
     * @throws InsufficientDataException thrown to indicate not enough data available in InputStream.
     * @throws InternalException         thrown to indicate internal library error.
     * @throws InvalidKeyException       thrown to indicate missing of HMAC SHA-256 library.
     * @throws InvalidResponseException  thrown to indicate S3 service returned invalid or no error response.
     * @throws IOException               thrown to indicate I/O error on S3 operation.
     * @throws NoSuchAlgorithmException  thrown to indicate missing of MD5 or SHA-256 digest library.
     * @throws XmlParserException        thrown to indicate XML parsing error.
     * @throws ServerException           ex
     */
    public void completePartUpload(
            String bucketName,
            String region,
            String objectName,
            String uploadId,
            Part[] parts,
            Multimap<String, String> extraHeaders,
            Multimap<String, String> extraQueryParams)
            throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException,
            ServerException, XmlParserException, ErrorResponseException, InternalException,
            InvalidResponseException {
        this.completeMultipartUpload(bucketName, region, objectName, uploadId, parts, extraHeaders, extraQueryParams);
    }

}
