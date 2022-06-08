package com.pongsky.kit.storage;

import com.pongsky.kit.storage.utils.MinIoUtils;
import io.minio.messages.Part;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author pengsenhao
 */
public class MinIoUtilsTest {

    /**
     * endpoint
     */
    private static final String endpoint = "http://localhost:9000";

    /**
     * bucket
     */
    private static final String bucket = "";

    /**
     * accessKey
     */
    private static final String accessKey = "";

    /**
     * secretKey
     */
    private static final String secretKey = "";

    /**
     * 文件名称
     */
    private static final String FILE_NAME = "/Users/pengsenhao/Downloads/test.jpeg";

    private static final MinIoUtils UTILS = new MinIoUtils(endpoint, bucket, accessKey, secretKey);

    public static void main(String[] args) throws IOException {
        File file = new File(FILE_NAME);
        String uploadId = UTILS.initPartUpload(file.getName(), "image/jpeg");
//        List<Part> parts = new ArrayList<>();
        // 每个分片的大小，用于计算文件有多少个分片。单位为字节。这里为 5 MB。
        final long partSize = 5 * 1024 * 1024L;
        long fileLength = file.length();
        int partCount = (int) (fileLength / partSize);
        // 遍历分片上传。
        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            InputStream instream = new FileInputStream(file);
            // 跳过已经上传的分片。
            instream.skip(startPos);
            Part part = UTILS.partUpload(uploadId, i + 1, (int) curPartSize, file.getName(), instream);
            // 可保存 part 信息 / 直接调用 listPart 方法获取所有分片信息，后续合并分片用。
//            parts.add(part);
        }
        List<Part> parts = UTILS.listPart(uploadId, file.getName());
        String url = UTILS.completePartUpload(uploadId, file.getName(), parts);
        System.out.println(url);
    }

}
