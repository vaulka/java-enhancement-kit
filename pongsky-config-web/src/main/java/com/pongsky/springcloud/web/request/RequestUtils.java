package com.pongsky.springcloud.web.request;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

/**
 * request 工具类
 *
 * @author pengsenhao
 */
public class RequestUtils {

    /**
     * 获取body数据
     *
     * @param request request
     * @return 获取body数据
     */
    public static String getBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
            if (StringUtils.isNotBlank(stringBuilder.toString())) {
                return stringBuilder.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取请求方法
     *
     * @param annotations 注解列表
     * @return 获取请求方法
     * @author pengsenhao
     */
    public static RequestMethod getMethod(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .map(a -> {
                    if (a instanceof GetMapping
                            || a instanceof PostMapping
                            || a instanceof PutMapping
                            || a instanceof DeleteMapping) {
                        return a.annotationType().getAnnotation(RequestMapping.class).method()[0];
                    } else if (a instanceof RequestMapping) {
                        return ((RequestMapping) a).method()[0];
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                // 没获取到默认 GET 请求
                .orElse(RequestMethod.GET);
    }

    /**
     * 获取请求 uri
     *
     * @param annotations 注解列表
     * @return 获取请求 uri
     * @author pengsenhao
     */
    public static String getUri(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .map(a -> {
                    if (a instanceof GetMapping) {
                        GetMapping mapping = (GetMapping) a;
                        return mapping.value().length > 0
                                ? mapping.value()[0]
                                : mapping.path().length > 0
                                ? mapping.path()[0]
                                : "";
                    } else if (a instanceof PutMapping) {
                        PutMapping mapping = (PutMapping) a;
                        return mapping.value().length > 0
                                ? mapping.value()[0]
                                : mapping.path().length > 0
                                ? mapping.path()[0]
                                : "";
                    } else if (a instanceof PostMapping) {
                        PostMapping mapping = (PostMapping) a;
                        return mapping.value().length > 0
                                ? mapping.value()[0]
                                : mapping.path().length > 0
                                ? mapping.path()[0]
                                : "";
                    } else if (a instanceof DeleteMapping) {
                        DeleteMapping mapping = (DeleteMapping) a;
                        return mapping.value().length > 0
                                ? mapping.value()[0]
                                : mapping.path().length > 0
                                ? mapping.path()[0]
                                : "";
                    } else if (a instanceof RequestMapping) {
                        RequestMapping mapping = (RequestMapping) a;
                        return mapping.value().length > 0
                                ? mapping.value()[0]
                                : mapping.path().length > 0
                                ? mapping.path()[0]
                                : "";
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("请求路径不存在"));
    }

}
