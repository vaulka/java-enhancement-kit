<p align="center">
    <a href="https://github.com/JarvisPongSky/java-enhancement-kit" target="_blank" rel="noopener noreferrer">
        <img width="100" src="https://www.pongsky.com/upload/2021/04/origin-logo.1-fc8493fcd5f449be9d87c141c1a93ac9.png" alt="Spring logo" />
    </a>
</p>

<p style="text-align:center"><b>Java 增强套件</b> 旨在减少重复造轮子以及封装一些实用功能，让开发者专注于业务，提高工作效率，值得一试。</p>

# 包含组件

## kit-captcha 验证码模块

### captcha-input-char 输入型字符 字符验证码模块

|模块|介绍|
|---|---|
|captcha-input-char-utils|输入型字符 验证码 Utils 模块|
|spring-boot-starter-captcha-input-char|输入型字符 验证码 Spring Boot Starter 模块|

### captcha-input-math 输入型算数 字符验证码模块

|模块|介绍|
|---|---|
|captcha-input-math-utils|输入型算数 验证码 Utils 模块|
|spring-boot-starter-captcha-input-math|输入型算数 验证码 Spring Boot Starter 模块|

## kit-desensitization 数据脱敏模块

|模块|介绍|
|---|---|
|desensitization-annotation|数据脱敏 Annotation 模块|
|desensitization-utils|数据脱敏 Utils 模块|
|spring-boot-starter-desensitization|数据脱敏 Spring Boot Starter 模块|

## kit-excel 读/写 Excel 模块

|模块|介绍|
|---|---|
|excel-utils|Excel Utils 模块|
|spring-boot-starter-excel|Excel Spring Boot Starter 模块|

## kit-storage 云存储模块

|模块|介绍|
|---|---|
|storage-annotation|云存储 Annotation 模块|
|storage-utils|云存储 Utils 模块|
|spring-boot-storage|云存储 Spring Boot 模块|
|spring-boot-starter-storage-oss|云存储 阿里云 OSS Spring Boot Starter 模块|
|spring-boot-starter-storage-minio|云存储 MinIO Spring Boot Starter 模块|

# 使用

> 版本号说明
>
> 版本由：`项目版本号`-`Spring Boot 版本`-`JDK 版本` 组成。
>
> 最新版本请点击 [Maven Repository](https://mvnrepository.com/search?q=pongsky) 查阅

## Maven 依赖

```xml
<dependency>
    <groupId>com.pongsky.kit</groupId>
    <artifactId>spring-boot-starter-captcha-input-math</artifactId>
    <version>${latestVersion}</version>
</dependency>
```

## Gradle 依赖

```groovy
implementation "com.pongsky.kit:spring-boot-starter-captcha-input-math:$latestVersion"
```
# 其他功能特点

* config-core
    * 全局实例ID（自动装配）
    * Redis 统一前缀以及配置（需手动引入）
    * Swagger 配置（自动装配）
    * 全局日志链路追踪（需配合 bootstrap.yml）
    * 异步配置（增强日志链路追踪）
    * Quartz 配置（增强日志链路追踪）
    * 定义异常以及全局响应信息

* config-web
    * Redis 模糊删除 Key
    * Feign 调用日志打印
    * Controller 调用统一日志打印
    * 接口防重校验
    * 可重复读取 Request Body

## 前置条件

* 需引入 `spring-boot-starter-data-redis` 依赖并配置相关 yml 信息
* 需配置 yml 信息，信息如下：

|参数|示例|说明|
|---|---|---|
|application.name|`@name@`|应用名称。<br />建议读取项目名称，并在 `bootstrap.yml` 中配置|
|application.module|gateway|模块名称。<br />建议在 `application.yml` 中配置|
|application.version|`@version@`|版本号。<br />建议读取项目版本号，并在 `bootstrap.yml` 中配置|
|application.formatted-version|`${application.name}-${application.module}-${application.version}`|打印版本号格式。<br />建议在 `application.yml` 中配置|
|spring.profiles.active|`dev`|环境，`local`、`dev`、`beta`、`prod`。<br />建议在 `bootstrap.yml` 中配置|
|spring.application.name|`${application.name}-${application.module}`|应用名称。<br />建议在 `application.yml` 中配置|

# 功能配置

## 全局实例ID

在 yml 或 `@Value` 中可直接引用 `${brokerId}` 获取当前实例ID。

> brokerId 由 系统 HostName、HostAddress 信息进行 MD5 后组成。
>
> 该场景适用于容器部署，每个实例的 HostName、HostAddress 组成唯一。

## Redis 统一前缀以及配置

防止与 `spring-boot-starter-data-redis` 自动装配冲突，请手动导入该 Bean。

```java
@Import({RedisConfig.class})
```

## 云存储配置

配置是否启用云存储，以及采用哪种云存储类型，详情查看 `additional-spring-configuration-metadata.json` 配置信息。

```yml
storage:
  type: aliyun # 云存储类型
  base-uri: https://storage.pongsky.com # 上传文件后，返回的路径自动拼接此URI
aliyun:
  oss:
    endpoint: oss-cn-shanghai-internal.aliyuncs.com # endpoint
    bucket: bucket # bucket
    access-key-id: key # access key
    access-key-secret: secret # access secret
minio:
  endpoint: minio.pongsky.com # endpoint
  bucket: bucket # bucket
  access-key: key # access key
  secret-key: secret # access secret
```

## 全局日志链路追踪

日志打印信息需添加打印 `X-Trace-Id` 参数。

```yml
logging:
  pattern:
    console: ${CONSOLE_LOG_PATTERN:%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}  %-5.5L{5}){cyan} %clr([%39X{X-Trace-Id}]){yellow} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:%wEx}} # 控制台日志打印格式
```

## Redis 模糊删除 Key

在方法上添加 `@CacheRemove` 使用。

```java
    @CacheRemove(keys = {
        "'MIHOYO::'+#rule.id+':*'",
        "'MIHOYO::'+#rule.userId+':*'"
})
```

## 数据脱敏

在字段上/方法上添加 `@DesensitizationMark` 使用，详情用法请查看该注解。

> WARN
>
> field type（字段类型）/ method returnType（方法返回值类型）必须为 String / Class

## 云存储URL自动添加显示前缀

在字段上/方法上添加 `@StorageResourceMark` 使用。

> WARN
>
> field type（字段类型）/ method returnType（方法返回值类型）必须为 String / Class

## 接口防重校验

在方法上添加 `@PreventDuplication` 使用，详情用法请查看该注解。

# 模块建议

> 按照此信息进行分模块，配合此套件能获得最佳的体验～

在多模块项目中建议按如下分包：

> `${project}`：项目名。
>
> `${service}`：服务名。

* `${project}`-common-entity
    * 依赖 `${project}`-common-utils
* `${project}`-common-utils
    * 依赖 `com.pongsky.kit:common-utils`
* `${project}`-config-core
    * 依赖 `${project}`-common-entity
    * 依赖 `com.pongsky.kit:config-core`
* `${project}`-config-core
    * 依赖 `${project}`-config-core
    * 依赖 `com.pongsky.kit:config-web`
* `${project}`-service-`${service}`
    * 依赖 `com.pongsky.kit:config-core` / `com.pongsky.kit:config-web`

# 代码、分包建议

> 按照此信息进行代码实装、分包，配合此套件能获得最佳的体验～
>
> `${basePackage}`：基础项目包，譬如 `com.pongsky.kit`。

## `${project}`-config-core

### 包：`${basePackage}`.config

譬如：com.pongsky.kit.config

#### BaseConfig

```java
/**
 * 基础配置
 *
 * @author pengsenhao
 */
@Component
@Import({RedisConfig.class})
public class BaseConfig {


}
```

### bootstrap.yml

```yml
application:
  name: @name@ # 工程名称
  version: @version@ # 工程版本号
spring:
  profiles:
    active: local # 选择配置文件
  security:
    user:
      name: ${application.name} # security 默认账号
      password: ${application.name} # security 默认密码
  web:
    locale: zh_CN # 简体中文语言
    resources:
      add-mappings: false # 不要为资源文件建立映射
  mvc:
    log-resolved-exception: true # 开启日志解析异常
    throw-exception-if-no-handler-found: true # 出现异常直接抛出错误
    format:
      time: HH:mm:ss # time 转换格式
      date: yyyy-MM-dd # date 转换格式
      date-time: yyyy-MM-dd HH:mm:ss # date-time 转换格式
  thymeleaf:
    check-template-location: false # 不检查模块位置
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss # 设置时间默认序列化格式
    time-zone: Asia/Shanghai # 设置序列化时区
    default-property-inclusion: non_null # 只序列化非空字段
  servlet:
    multipart:
      max-file-size: 100MB # 设置最大文件上传大小 100MB
      max-request-size: 100MB # 设置发起请求最大文件上传大小 100MB
  task:
    execution:
      pool:
        core-size: 10 # 定时任务线程池核心数
        max-size: 100 # 定时任务最大线程数
        keep-alive: 30s # 定时任务线程保持空闲时间
  output:
    ansi:
      enabled: always # 启用控制台打印颜色
  cloud:
    consul:
      discovery:
        health-check-critical-timeout: 10s # 设置健康检查失败多长时间后，取消注册
        tags:
          - ${spring.profiles.active}-@version@ # 版本号，通过与 server-list-query-tags 参数关联，实现服务隔离
        server-list-query-tags:
          genshin-adventure-mihoyo: ${spring.profiles.active}-@version@
          genshin-adventure-gateway: ${spring.profiles.active}-@version@
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      minimum-idle: 5
      maximum-pool-size: 50
      auto-commit: true
      idle-timeout: 600000
      pool-name: DatebookHikariCP
      max-lifetime: 1800000
      connection-timeout: 30000
      connection-test-query: SELECT 1
  rabbitmq:
    template:
      reply-timeout: 10000 # sendAndReceive()方法的超时时间，单位毫秒
logging:
  pattern:
    console: ${CONSOLE_LOG_PATTERN:%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}  %-5.5L{5}){cyan} %clr([%39X{X-Trace-Id}]){yellow} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:%wEx}} # 控制台日志打印格式
server:
  shutdown: graceful # 优雅关机
management:
  endpoints:
    web:
      exposure:
        include: "*" # 以 web 方式暴露所有监控端点
  endpoint:
    health:
      show-details: always # 总是显示健康详情
mybatis-plus:
  global-config:
    banner: false # 关闭 mybatis-plus banner 打印
    db-config:
      id-type: input # 主键策略
```

## `${project}`-config-web

### 包：`${basePackage}`.security

譬如：com.pongsky.kit.security

> 基于 `spring-boot-starter-security` 拦截。

#### SecurityConfig

```java
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 鉴权配置
 *
 * @author pengsenhao
 */
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TraceFilter traceFilter;
    private final AuthenticationFilter authenticationFilter;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置跨域请求
     *
     * @param http http
     * @throws Exception 异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource())
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(traceFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 配置跨域请求
     *
     * @return 跨域请求
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        List<String> methods = Stream.of(RequestMethod.PATCH, RequestMethod.OPTIONS,
                        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE)
                .map(Enum::toString)
                .collect(Collectors.toList());
        corsConfiguration.setAllowedMethods(methods);
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(0L);
        corsConfiguration.addExposedHeader(HttpHeaders.AUTHORIZATION);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
```

#### TraceFilter

```java
import CurrentInfo;
import CurrentThreadConfig;
import DiyHeader;
import com.pongsky.kit.web.request.Whitelist;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 链路拦截器
 *
 * @author pengsenhao
 */
@Component
public class TraceFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Whitelist.URLS.contains(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain chain) throws IOException, ServletException {
        String traceId = CurrentThreadConfig.buildTraceId(request);
        MDC.put(DiyHeader.X_TRACE_ID, traceId);
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> requestHeaders = new HashMap<>(16);
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            requestHeaders.put(key, value);
        }
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        CurrentInfo currentInfo = new CurrentInfo()
                .setTraceId(traceId)
                .setAuthorization(authorization)
                .setRequestHeaders(requestHeaders);
        CurrentThreadConfig.setCurrentInfo(currentInfo);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
            CurrentThreadConfig.relCurrentInfo();
        }
    }

}
```

## `${project}`-service-`${service}`

### 包：`${basePackage}`.config

譬如：com.pongsky.kit.config

#### LocalConfig

```java
/**
 * @author pengsenhao
 */
@Component
@Import({MyBatisConfig.class, AsyncConfig.class})
public class LocalConfig {


}
```

#### QuartzConfig

```java
import QuartzUtils;
import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pengsenhao
 * @create 2021-02-25
 */
@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final Scheduler scheduler;

    @Bean
    public QuartzUtils quartzUtils() {
        QuartzUtils quartzUtils = new QuartzUtils(scheduler);
        // xxx.quartzUtils = quartzUtils;
        return quartzUtils;
    }

}
```

### 包：`${basePackage}`.controller

譬如：com.pongsky.kit.controller

用于放置所有 controller 接口。

#### 包：`${basePackage}`.controller.api

譬如：com.pongsky.kit.controller.api

该包下接口用于第三方调用。

#### 包：`${basePackage}`.controller.console

譬如：com.pongsky.kit.controller.console

该包下接口用于前端项目调用。

#### 包：`${basePackage}`.controller.service

譬如：com.pongsky.kit.controller.service

该包下接口用于后端项目远程调用。

### 包：`${basePackage}`.job

譬如：com.pongsky.kit.job

用于放置所有 job 定时任务调度。

### 包：`${basePackage}`.mapper

譬如：com.pongsky.kit.mapper

用于放置所有 Mapper。

### 包：`${basePackage}`.service

譬如：com.pongsky.kit.service

用于放置所有 Service。

#### 包：`${basePackage}`.service.impl

譬如：com.pongsky.kit.service.impl

用于放置所有 Service Impl。

### banner.txt

logo 自行替换成项目模块。

```txt
  ██╗ ██╗ █████╗ ██████╗ ██╗   ██╗███████╗███╗   ██╗████████╗██╗   ██╗██████╗ ███████╗    ███╗   ███╗██╗██╗  ██╗ ██████╗ ██╗   ██╗ ██████╗ ██╗ ██╗
 ██╔╝██╔╝██╔══██╗██╔══██╗██║   ██║██╔════╝████╗  ██║╚══██╔══╝██║   ██║██╔══██╗██╔════╝    ████╗ ████║██║██║  ██║██╔═══██╗╚██╗ ██╔╝██╔═══██╗╚██╗╚██╗
██╔╝██╔╝ ███████║██║  ██║██║   ██║█████╗  ██╔██╗ ██║   ██║   ██║   ██║██████╔╝█████╗█████╗██╔████╔██║██║███████║██║   ██║ ╚████╔╝ ██║   ██║ ╚██╗╚██╗
╚██╗╚██╗ ██╔══██║██║  ██║╚██╗ ██╔╝██╔══╝  ██║╚██╗██║   ██║   ██║   ██║██╔══██╗██╔══╝╚════╝██║╚██╔╝██║██║██╔══██║██║   ██║  ╚██╔╝  ██║   ██║ ██╔╝██╔╝
 ╚██╗╚██╗██║  ██║██████╔╝ ╚████╔╝ ███████╗██║ ╚████║   ██║   ╚██████╔╝██║  ██║███████╗    ██║ ╚═╝ ██║██║██║  ██║╚██████╔╝   ██║   ╚██████╔╝██╔╝██╔╝
  ╚═╝ ╚═╝╚═╝  ╚═╝╚═════╝   ╚═══╝  ╚══════╝╚═╝  ╚═══╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚══════╝    ╚═╝     ╚═╝╚═╝╚═╝  ╚═╝ ╚═════╝    ╚═╝    ╚═════╝ ╚═╝ ╚═╝

Application Version: ${application.formatted-version}-${application.instance-id}
Spring Boot Version: ${spring-boot.formatted-version}
```

### application.yml

```yml
application:
  module: mihoyo # 模块名称
  formatted-version: ${application.name}-${application.module}-${application.version} # 格式化版本
spring:
  application:
    name: ${application.name}-${application.module} # 应用名称
  cloud:
    consul:
      discovery:
        service-name: ${spring.profiles.active}-${spring.application.name} # 服务名称
        hostname: ${spring.profiles.active}-${spring.application.name} # hostname 名称
        instance-id: ${spring.application.name}-${application.instance-id} # 唯一实例ID
  # 集群定时任务 配置
  quartz:
    job-store-type: jdbc
    wait-for-jobs-to-complete-on-shutdown: true
    properties:
      org:
        quartz:
          scheduler:
            instanceName: clusteredScheduler
            instanceId: AUTO
          jobStore:
            acquireTriggersWithinLock: true
            class: org.quartz.impl.jdbcjobstore.JobStoreTX
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
            tablePrefix: QRTZ_
            isClustered: true
            clusterCheckinInterval: 10000
            misfireThreshold: 60000
            maxMisfiresToHandleAtATime: 1
            useProperties: false
          threadPool:
            class: org.quartz.simpl.SimpleThreadPool
            threadCount: 50
            threadPriority: 5
            threadsInheritContextClassLoaderOfInitializingThread: true
```
