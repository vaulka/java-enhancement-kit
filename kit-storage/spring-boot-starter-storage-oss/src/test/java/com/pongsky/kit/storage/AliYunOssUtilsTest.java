package com.pongsky.kit.storage;

import com.aliyun.oss.model.PartETag;
import com.pongsky.kit.storage.utils.AliYunOssUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengsenhao
 */
public class AliYunOssUtilsTest {

    /**
     * endpoint
     */
    private static final String endpoint = "oss-cn-shanghai.aliyuncs.com";

    /**
     * bucket
     */
    private static final String bucket = "";

    /**
     * accessKeyId
     */
    private static final String accessKeyId = "";

    /**
     * secretAccessKey
     */
    private static final String secretAccessKey = "";

    /**
     * 文件名称
     */
    private static final String FILE_NAME = "/Users/pengsenhao/Downloads/----4k.jpg";

    private static final AliYunOssUtils UTILS = new AliYunOssUtils(endpoint, bucket, accessKeyId, secretAccessKey);

    public static void main(String[] args) throws IOException {
        File file = new File(FILE_NAME);
        String uploadId = UTILS.initPartUpload(file.getName());
        List<PartETag> partETags = new ArrayList<>();
        // 每个分片的大小，用于计算文件有多少个分片。单位为字节。这里为 1 MB。
        final long partSize = 1 * 1024 * 1024L;
        long fileLength = file.length();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        // 遍历分片上传。
        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            InputStream instream = new FileInputStream(file);
            // 跳过已经上传的分片。
            instream.skip(startPos);
            PartETag partETag = UTILS.partUpload(uploadId, i + 1, curPartSize, file.getName(), instream);
            partETags.add(partETag);
        }
        String url = UTILS.completePartUpload(uploadId, file.getName(), partETags);
        System.out.println(url);
    }

}
