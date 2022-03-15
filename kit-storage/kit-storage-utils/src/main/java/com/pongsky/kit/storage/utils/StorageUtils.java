package com.pongsky.kit.storage.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.InputStream;

/**
 * 云存储工具类
 *
 * @author pengsenhao
 */
public interface StorageUtils {

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
     * @author pengsenhao
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
     *
     * @author pengsenhao
     */
    void createBucket();

    /**
     * 文件上传
     *
     * @param fileName    文件名称
     * @param contentType 文件类型
     * @param inputStream input 流
     * @return 文件访问路径
     * @author pengsenhao
     */
    String upload(String fileName, String contentType, InputStream inputStream);

}
