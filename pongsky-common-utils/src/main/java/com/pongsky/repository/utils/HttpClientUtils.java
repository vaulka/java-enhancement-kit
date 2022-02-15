package com.pongsky.repository.utils;


import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

/**
 * JDK11 Http Client工具类
 *
 * @author pengsenhao
 * @create 2019-10-16 16:16
 */
@Slf4j
public class HttpClientUtils {

    /**
     * 成功状态码
     */
    private static final int STATUS_OK = 200;

    /**
     * 创建 HttpClient
     */
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            // 设置版本号 HTTP2
            .version(HttpClient.Version.HTTP_2)
            // 设置连接超时，单位毫秒，10000ms = 10s
            .connectTimeout(Duration.ofMillis(10000))
            // 自动重定向
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    /**
     * 发送 POST 请求，返回 JSON 数据
     *
     * @param url     访问链接
     * @param headers 请求头集合
     * @param body    请求数据
     * @return JSON 数据
     * @throws Exception 异常
     */
    public static HttpResponse<String> sendJsonPostRequest(String url,
                                                           Map<String, String> headers,
                                                           Map<String, Object> body) throws Exception {
        // 创建 Builder
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                // 设置 POST 请求，并将 body 参数放入
                .POST(body == null ?
                        HttpRequest.BodyPublishers.noBody()
                        : HttpRequest.BodyPublishers.ofString(JsonUtils.toJsonString(body)));
        return getJsonResultBody(builder, url, headers);
    }

    /**
     * 发送 POST 请求，返回 JSON 数据
     *
     * @param url     访问链接
     * @param headers 请求头集合
     * @param body    请求数据
     * @return JSON 数据
     * @throws Exception 异常
     */
    public static HttpResponse<String> sendJsonPostRequest(String url,
                                                           Map<String, String> headers,
                                                           String body) throws Exception {
        // 创建 Builder
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                // 设置 POST 请求，并将 body 参数放入
                .POST(body == null ?
                        HttpRequest.BodyPublishers.noBody()
                        : HttpRequest.BodyPublishers.ofString(body));
        return getJsonResultBody(builder, url, headers);
    }


    /**
     * 发送 POST 请求，返回 byte[] 数据
     *
     * @param url     访问链接
     * @param headers 请求头集合
     * @param body    请求数据
     * @return byte[] 数据
     * @throws Exception 异常
     */
    public static HttpResponse<byte[]> sendBytesPostRequest(String url,
                                                            Map<String, String> headers,
                                                            Map<String, Object> body) throws Exception {
        // 创建 Builder
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                // 设置 POST 请求，并将 body 参数放入
                .POST(body == null ?
                        HttpRequest.BodyPublishers.noBody()
                        : HttpRequest.BodyPublishers.ofString(JsonUtils.toJsonString(body)));
        return getBytesResultBody(builder, url, headers);
    }

    /**
     * 发送 POST 请求，返回 inputStream 数据
     *
     * @param url     访问链接
     * @param headers 请求头集合
     * @param body    请求数据
     * @return inputStream 数据
     * @throws Exception 异常
     */
    public static HttpResponse<InputStream> sendInputStreamPostRequest(String url,
                                                                       Map<String, String> headers,
                                                                       String body) throws Exception {
        // 创建 Builder
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                // 设置 POST 请求，并将 body 参数放入
                .POST(body == null ?
                        HttpRequest.BodyPublishers.noBody()
                        : HttpRequest.BodyPublishers.ofString(body));
        return getInputStreamResultBody(builder, url, headers);
    }

    /**
     * 发送 GET 请求，返回 inputStream 数据
     *
     * @param url     访问链接
     * @param headers 请求头集合
     * @return inputStream 数据
     * @throws Exception 异常
     */
    public static HttpResponse<InputStream> sendInputStreamGetRequest(String url,
                                                                      Map<String, String> headers) throws Exception {
        // 创建 Builder
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                // 设置 GET 请求
                .GET();
        return getInputStreamResultBody(builder, url, headers);
    }

    /**
     * 发送 GET 请求，返回 byte[] 数据
     *
     * @param url     访问链接
     * @param headers 请求头集合
     * @return byte[] 数据
     * @throws Exception 异常
     */
    public static HttpResponse<byte[]> sendBytesGetRequest(String url,
                                                           Map<String, String> headers) throws Exception {
        // 创建 Builder
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                // 设置 POST 请求，并将 body 参数放入
                .GET();
        return getBytesResultBody(builder, url, headers);
    }

    /**
     * 发送 GET 请求，返回 JSON 数据
     *
     * @param url     访问链接
     * @param headers 请求头集合
     * @return JSON 数据
     * @throws Exception 异常
     */
    public static HttpResponse<String> sendJsonGetRequest(String url,
                                                          Map<String, String> headers) throws Exception {
        // 创建 Builder
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                // 设置 GET 请求
                .GET();
        return getJsonResultBody(builder, url, headers);
    }

    /**
     * 赋值请求头并获取HttpRequest
     *
     * @param builder builder
     * @param url     请求链接
     * @param headers 请求头集合
     * @return HttpRequest
     */
    private static HttpRequest setHeaderAndGetHttpRequest(HttpRequest.Builder builder,
                                                          String url,
                                                          Map<String, String> headers) {
        // 设置 headers 请求头
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // 创建 HttpRequest
        return builder
                // 设置访问链接
                .uri(URI.create(url))
                // 设置版本号 HTTP2
                .version(HttpClient.Version.HTTP_2)
                // 设置请求超时，单位毫秒，7000ms = 7s
                .timeout(Duration.ofMillis(7000))
                .build();
    }

    /**
     * 发送请求，获取 JSON 数据
     *
     * @param builder builder
     * @param headers 请求头集合
     * @return JSON 数据
     * @throws Exception 异常
     */
    private static HttpResponse<String> getJsonResultBody(HttpRequest.Builder builder,
                                                          String url,
                                                          Map<String, String> headers) throws Exception {
        // 赋值请求头
        HttpRequest request = setHeaderAndGetHttpRequest(builder, url, headers);
        // 创建 HttpResponse
        HttpResponse<String> response;
        try {
            response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new Exception(e.getLocalizedMessage(), e);
        }
        // 获取请求状态码
        int statusCode = response.statusCode();
        // 请求状态码只有200响应正确
        if (statusCode != STATUS_OK) {
            throw new Exception(response.body());
        }
        return response;
    }

    /**
     * 发送请求，获取 byte[] 数据
     *
     * @param builder builder
     * @param headers 请求头集合
     * @return byte[] 数据
     * @throws Exception 异常
     */
    private static HttpResponse<byte[]> getBytesResultBody(HttpRequest.Builder builder,
                                                           String url,
                                                           Map<String, String> headers) throws Exception {
        // 赋值请求头
        HttpRequest request = setHeaderAndGetHttpRequest(builder, url, headers);
        // 创建 HttpResponse
        HttpResponse<byte[]> response;
        try {
            response = CLIENT.send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (Exception e) {
            throw new Exception(e.getLocalizedMessage(), e);
        }
        // 获取请求状态码
        int statusCode = response.statusCode();
        // 请求状态码只有200响应正确
        if (statusCode != STATUS_OK) {
            throw new Exception(Arrays.toString(response.body()));
        }
        return response;
    }

    /**
     * 发送请求，获取 InputStream 数据
     *
     * @param builder builder
     * @param headers 请求头集合
     * @return InputStream 数据
     * @throws Exception 异常
     */
    private static HttpResponse<InputStream> getInputStreamResultBody(HttpRequest.Builder builder,
                                                                      String url,
                                                                      Map<String, String> headers) throws Exception {
        // 赋值请求头
        HttpRequest request = setHeaderAndGetHttpRequest(builder, url, headers);
        // 创建 HttpResponse
        HttpResponse<InputStream> response;
        try {
            response = CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
        } catch (Exception e) {
            throw new Exception(e.getLocalizedMessage(), e);
        }
        // 获取请求状态码
        int statusCode = response.statusCode();
        // 请求状态码只有200响应正确
        if (statusCode != STATUS_OK) {
            throw new Exception(Arrays.toString(response.body().readAllBytes()));
        }
        return response;
    }

    /**
     * 休眠
     *
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-01-09 9:07 下午
     */
    public static void sleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            log.error(e.getLocalizedMessage());
        }
    }

}
