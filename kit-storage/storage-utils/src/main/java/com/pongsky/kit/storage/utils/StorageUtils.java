package com.pongsky.kit.storage.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.InputStream;
import java.util.List;

/**
 * 云存储工具类
 *
 * @author pengsenhao
 */
public interface StorageUtils<T> {

    /**
     * 点 符号
     */
    String POINT = ".";

    /**
     * 构建文件访问路径
     *
     * @param fileName 文件名称
     * @param args     参数列表
     * @return 文件访问路径
     */
    default String buildFileName(String fileName, String... args) {
        StringBuilder sb = new StringBuilder();
        if (args != null && args.length > 0) {
            sb.append(String.join("/", args)).append("/");
        }
        if (fileName.contains(POINT)) {
            sb.append(fileName, 0, fileName.lastIndexOf(POINT))
                    .append("-")
                    .append(RandomStringUtils.randomAlphabetic(5))
                    .append("-")
                    .append(System.currentTimeMillis())
                    .append(fileName.substring(fileName.lastIndexOf(POINT)));
        } else {
            sb.append(fileName)
                    .append("-")
                    .append(RandomStringUtils.randomAlphabetic(5))
                    .append("-")
                    .append(System.currentTimeMillis());
        }
        return sb.toString();
    }

    /***
     *  创建 bucket（如果不存在，则自动创建）
     */
    void createBucket();

    /**
     * 文件上传
     *
     * @param fileName    文件名称
     * @param contentType 文件类型
     * @param inputStream input 流
     * @return 文件访问路径
     */
    String upload(String fileName, String contentType, InputStream inputStream);

    /**
     * 获取分片上传ID
     *
     * @param fileName 文件名称
     * @return 分片上传ID
     */
    String initPartUpload(String fileName);

    /**
     * 分片上传
     *
     * @param uploadId    分片上传事件ID
     * @param partNumber  当前分片数
     * @param partSize    当前分片文件大小，单位为字节，譬如：1MB = 1 * 1024 * 1024L
     * @param fileName    文件名称
     * @param inputStream input 流
     * @return 分片ID
     */
    T partUpload(String uploadId, Integer partNumber, Long partSize, String fileName, InputStream inputStream);

    /**
     * 合并分片上传
     *
     * @param uploadId 分片上传事件ID
     * @param fileName 文件名称
     * @param parts    分片信息列表
     * @return 文件访问路径
     */
    String completePartUpload(String uploadId, String fileName, List<T> parts);

}
