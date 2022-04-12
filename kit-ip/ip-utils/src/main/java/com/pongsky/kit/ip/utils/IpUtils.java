package com.pongsky.kit.ip.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * IP 工具类
 *
 * @author pengsenhao
 **/
public class IpUtils {

    /**
     * 未知
     */
    private static final String UNKNOWN = "unknown";

    /**
     * 本地 IP　列表
     */
    private static final List<String> LOCAL_HOST_IPS = Arrays.asList("127.0.0.1", "0:0:0:0:0:0:0:1");

    /**
     * 获取 IP
     *
     * @param request request
     * @return 获取 IP
     */
    public static String getIp(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        // 获取请求主机 IP 地址,如果通过代理进来，则透过防火墙获取真实 IP 地址
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Real-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCAL_HOST_IPS.contains(ipAddress)) {
                // 根据网卡取本机配置的 IP
                return IpUtils.getHostAddress();
            }
        }
        return ipAddress;
    }


    /**
     * 获取本机 IP
     *
     * @return 获取本机 IP
     */
    private static String getHostAddress() {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
        return inetAddress.getHostAddress();
    }

    /**
     * 查询 IP 地址网站
     */
    private static final String QUERY_IP_URL = "https://whois.pconline.com.cn/ip.jsp?ip={0}";

    /**
     * OK Http Client
     */
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    /**
     * 获取 IP 对应的所在地区
     *
     * @param ip IP
     * @return 获取 IP 对应的所在地区
     */
    public static String getAddress(String ip) {
        Request request = new Request.Builder()
                .url(MessageFormat.format(QUERY_IP_URL, ip))
                .build();
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            ResponseBody body = response.body();
            return body == null
                    ? null
                    : body.string();
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

}
