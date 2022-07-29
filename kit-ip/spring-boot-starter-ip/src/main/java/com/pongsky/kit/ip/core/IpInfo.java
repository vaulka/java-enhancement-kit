package com.pongsky.kit.ip.core;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * IP 信息
 *
 * @author pengsenhao
 */
@Data
public class IpInfo implements Serializable {

    private static final long serialVersionUID = -9032761088785200116L;

    /**
     * 格式规则
     */
    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");

    /**
     * 格式位数
     */
    private static final int SIZE = 5;

    public IpInfo(String address) {
        if (address == null) {
            return;
        }
        String[] splitInfos = SPLIT_PATTERN.split(address);
        if (splitInfos.length < SIZE) {
            splitInfos = Arrays.copyOf(splitInfos, SIZE);
        }
        this.country = this.getFilterAddress(splitInfos[0]);
        this.region = this.getFilterAddress(splitInfos[1]);
        this.province = this.getFilterAddress(splitInfos[2]);
        this.city = this.getFilterAddress(splitInfos[3]);
        this.isp = this.getFilterAddress(splitInfos[4]);
    }

    /**
     * ip2Region 采用 0 填充的没有数据的字段
     */
    private static final String ZERO = "0";

    /**
     * 获取过滤后的地址
     *
     * @param address 地址
     * @return 获取过滤后的地址
     */
    private String getFilterAddress(String address) {
        if (address == null || ZERO.equals(address)) {
            return null;
        }
        return address;
    }

    /**
     * 国家
     */
    private String country;

    /**
     * 区域
     */
    private String region;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 运营商
     */
    private String isp;

    /**
     * 拼接完整的地址
     *
     * @return address
     */
    public String getAddress() {
        return Stream.of(country, region, province, city, isp)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }

}
