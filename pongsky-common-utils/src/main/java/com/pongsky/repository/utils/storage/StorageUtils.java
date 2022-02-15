package com.pongsky.repository.utils.storage;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.InputStream;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-02-13 6:36 下午
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
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-02-13 6:44 下午
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

    /**
     * 文件上传
     *
     * @param fileName    文件名称
     * @param inputStream input 流
     * @return 文件访问路径
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-02-13 6:37 下午
     */
    String upload(String fileName, InputStream inputStream);

}
