package com.pongsky.springcloud.config.property;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-02-07 3:14 下午
 */
public class Instance {

    /**
     * 获取实例ID
     *
     * @return 实例ID
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-02-07 3:16 下午
     */
    public String getId() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new Error("获取本地IP失败");
        }
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new Error(e.getMessage());
        }
        // Use specified byte update digest.
        md.update(address.getHostName().getBytes());
        md.update(address.getHostAddress().getBytes());
        //Get cipher text
        byte[] b = md.digest();
        // The cipher text converted to hexadecimal string
        StringBuilder su = new StringBuilder();
        // byte array switch hexadecimal number.
        for (byte value : b) {
            String haxHex = Integer.toHexString(value & 0xFF);
            if (haxHex.length() < 2) {
                su.append("0");
            }
            su.append(haxHex);
        }
        return Long.valueOf(su.substring(20), 16).toString();
    }

}
