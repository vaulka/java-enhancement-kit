package com.pongsky.kit.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * IP 工具类
 *
 * @author pengsenhao
 */
public class IpUtils {

    private static final String UNKNOWN = "unknown";

    private static final List<String> LOCALHOST = List.of("127.0.0.1", "0:0:0:0:0:0:0:1");

    public static String getIp(HttpServletRequest request) {

        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCALHOST.contains(ipAddress)) {
                //根据网卡取本机配置的IP
                InetAddress inetAddress;
                try {
                    inetAddress = InetAddress.getLocalHost();
                    return inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        if (UNKNOWN.equalsIgnoreCase(ipAddress)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ipAddress.indexOf(",");
            if (index != -1) {
                return ipAddress.substring(0, index);
            } else {
                return ipAddress;
            }
        }
        ipAddress = request.getHeader("X-Real-IP");
        if (!StringUtils.isEmpty(ipAddress) && !UNKNOWN.equalsIgnoreCase(ipAddress)) {
            return ipAddress;
        }
        return request.getRemoteAddr();
        /*//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;*/
    }

}
