package com.pongsky.kit.ip.config;

import com.pongsky.kit.ip.properties.IpProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import java.io.InputStream;

/**
 * IP 搜索器配置
 *
 * @author pengsenhao
 */
@Slf4j
@RequiredArgsConstructor
public class IpSearcherConfig implements InitializingBean, DisposableBean, IpSearcher {

    private final IpProperties properties;
    private final ResourceLoader resourceLoader;

    private Searcher searcher;

    /**
     * 初始化 IP 地址池信息
     *
     * @throws Exception 异常
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Resource resource = resourceLoader.getResource(properties.getDbPath());
        try (InputStream inputStream = resource.getInputStream()) {
            searcher = Searcher.newWithBuffer(StreamUtils.copyToByteArray(inputStream));
        }
    }

    /**
     * 关闭资源
     *
     * @throws Exception 异常
     */
    @Override
    public void destroy() throws Exception {
        searcher.close();
    }

    /**
     * 根据 IP 获取地址
     * <p>
     * 每个 ip 数据段的 region 信息都固定了格式：国家|区域|省份|城市|ISP，只有中国的数据绝大部分精确到了城市，其他国家部分数据只能定位到国家，后前的选项全部是0。
     *
     * @param ip IP
     * @return 地址
     */
    @Override
    public String getAddress(String ip) {
        try {
            return searcher.search(ip);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }

}
