package com.pongsky.kit.web.core.fitler;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 替换 HttpServletRequest，实现 request body 数据多次读取
 *
 * @author pengsenhao
 */
public class ReplaceStreamFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * 上传文件请求头列表
     */
    private static final List<String> UPLOAD_FILE_CONTENT_TYPES = Arrays.asList(
            "image/bmp",
            "image/gif",
            "image/jpg",
            "image/jpeg",
            "image/png",
            "image/svg+xml",
            "image/tiff",
            "image/webp",
            "multipart/form-data",
            "multipart/mixed",
            "multipart/related"
    );

    /**
     * 判断是否缓存 request body
     *
     * @param request request
     * @return 判断是否缓存 request body
     */
    public boolean isCacheRequestBody(ServletRequest request) {
        if (request.getContentType() == null) {
            return false;
        }
        return UPLOAD_FILE_CONTENT_TYPES.stream()
                .noneMatch(contentType -> request.getContentType().contains(contentType));
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        if (this.isCacheRequestBody(request)) {
            chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

}
