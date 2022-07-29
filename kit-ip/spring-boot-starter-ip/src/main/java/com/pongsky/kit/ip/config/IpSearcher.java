package com.pongsky.kit.ip.config;

/**
 * IP 搜索器
 *
 * @author pengsenhao
 */
public interface IpSearcher {

    /**
     * 根据 IP 获取地址
     * <p>
     * 每个 ip 数据段的 region 信息都固定了格式：国家|区域|省份|城市|ISP，只有中国的数据绝大部分精确到了城市，其他国家部分数据只能定位到国家，后前的选项全部是0。
     *
     * @param ip IP
     * @return 地址
     */
    String getAddress(String ip);

}
