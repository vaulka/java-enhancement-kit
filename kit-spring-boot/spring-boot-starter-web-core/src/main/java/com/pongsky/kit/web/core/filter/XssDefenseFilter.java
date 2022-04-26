package com.pongsky.kit.web.core.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * XSS 防御
 *
 * @author pengsenhao
 */
public class XssDefenseFilter implements Filter {

    /**
     * 排除列表
     */
    private final List<Pattern> excludePatterns;


    public XssDefenseFilter(List<String> excludes) {
        this.excludePatterns = excludes.stream()
                .map(e -> Pattern.compile("^" + e))
                .collect(Collectors.toList());
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();
        boolean noIsHit = excludePatterns.stream()
                .noneMatch(ep -> ep.matcher(uri).find());
        ServletRequest servletRequest = request;
        // 如果在放行列表，则不做拦截处理
        if (noIsHit) {
            servletRequest = new XssDefenseRequestWrapper((HttpServletRequest) request);
        }
        chain.doFilter(servletRequest, response);
    }

    @Override
    public void destroy() {
    }

}
