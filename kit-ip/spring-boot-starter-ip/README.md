# spring-boot-starter-ip 模块说明

> IP Spring Boot Starter 模块
>
> 该模块依赖
> * [ip2region](https://github.com/lionsoul2014/ip2region)

## 功能说明

* 通过 ip 解析出对应地址。

## 配置 IP 参数

在 `yml` 配置 IP 配置信息，参数如下：

|参数|是否可空|描述|默认值|
|---|---|---|---|
|ip.db-path|true|ip2region.xdb 文件路径|classpath:ip2region/ip2region.xdb|

示例如下：

```yml

ip:
   db-path: classpath:ip2region/ip2region.xdb

```

## 使用

### 通过 ip 解析出对应地址

```java

@Service
@RequiredArgsConstructor
public class TestService {

    private final IpSearcher ipSearcher;

    @PostConstruct
    public void test() {
        String address = ipSearcher.getAddress("1.2.3.4");
        log.info("地址：{}", address);
    }
}

```
