<p style="text-align:center">
    <a href="https://github.com/JarvisPongSky/spring-cloud-enhancement-kit" target="_blank" rel="noopener noreferrer">
        <img width="100" src="https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg" alt="Spring logo" />
    </a>
</p>

<p style="text-align:center"><b>Spring Cloud 增强套件</b> 旨在减少重复造轮子以及统一模块规范，值得一试。</p>

> 本项目基于 `Spring Boot 2.5.6` 及对应 `Spring Cloud` 版本进行构建。

# 功能特点

* config-core
    * 全局实例ID（自动装配）
    * Redis 统一前缀以及配置（需手动引入）
    * MyBatis 配置（需手动引入）
    * 云存储配置（yml 启用以及配置相关信息）
    * Swagger 配置（自动装配）
    * 全局日志链路追踪（需配合 bootstrap.yml）
    * 异步配置（增强日志链路追踪）
    * Quartz 配置（增强日志链路追踪）
    * 定义异常以及全局响应信息

* config-web
    * Redis 模糊删除 Key
    * Feign 调用日志打印
    * Controller 调用统一日志打印
    * 数据脱敏
    * 云存储URL自动添加显示前缀
        * 文件上传判空校验
        * 启用云存储自动装配云存储 Controller
    * 接口防重校验
    * 可重复读取 Request Body

# 使用

> 版本号说明
>
> 版本由：`Spring Boot 版本`-`迭代版本` 组成。
>
> 譬如 `2.5.6-1.0.0` 则是 `增强套件 1.0.0`  基于 `Spring Boot 2.5.6` 构建。

> 依赖引用说明
>
> `config-core` 依赖 `common-utils`。
>
> `config-web` 依赖 `config-core`。

## Maven 依赖

```xml

<dependency>
    <groupId>com.pongsky.springcloud</groupId>
    <artifactId>common-utils</artifactId>
    <version>${version}</version>
</dependency>
```

```xml

<dependency>
    <groupId>com.pongsky.springcloud</groupId>
    <artifactId>config-core</artifactId>
    <version>${version}</version>
</dependency>
```

```xml

<dependency>
    <groupId>com.pongsky.springcloud</groupId>
    <artifactId>config-web</artifactId>
    <version>${version}</version>
</dependency>
```

## Gradle 依赖

```groovy
api "com.pongsky.springcloud:common-utils:$version"
```

```groovy
api "com.pongsky.springcloud:config-core:$version"
```

```groovy
api "com.pongsky.springcloud:config-web:$version"
```

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

## MyBatis 配置

目前就加入了乐观锁配置，如需使用请手动导入该 Bean。

```java
@Import({MyBatisConfig.class})
```

## 云存储配置

配置是否启用云存储，以及采用哪种云存储类型，详情查看 `additional-spring-configuration-metadata.json` 配置信息。

```yml
storage:
  enable: true # 云存储是否启用
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
    * 依赖 `com.pongsky.springcloud:common-utils`
* `${project}`-config-core
    * 依赖 `${project}`-common-entity
    * 依赖 `com.pongsky.springcloud:config-core`
* `${project}`-config-core
    * 依赖 `${project}`-config-core
    * 依赖 `com.pongsky.springcloud:config-web`
* `${project}`-service-`${service}`
    * 依赖 `com.pongsky.springcloud:config-core` / `com.pongsky.springcloud:config-web`

# 代码、分包建议

> 按照此信息进行代码实装、分包，配合此套件能获得最佳的体验～
>
> `${basePackage}`：基础项目包，譬如 `com.pongsky.springcloud`。

## `${project}`-config-core

### 包：`${basePackage}`.config

譬如：com.pongsky.springcloud.config

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

譬如：com.pongsky.springcloud.security

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
import com.pongsky.springcloud.utils.trace.CurrentInfo;
import com.pongsky.springcloud.utils.trace.CurrentThreadConfig;
import com.pongsky.springcloud.utils.trace.DiyHeader;
import com.pongsky.springcloud.web.request.Whitelist;
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

### 包：`${basePackage}`.web.handler

譬如：com.pongsky.springcloud..web.handler

#### GlobalExceptionHandler

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.springcloud.exception.DeleteException;
import com.pongsky.springcloud.exception.DoesNotExistException;
import com.pongsky.springcloud.exception.ExistException;
import com.pongsky.springcloud.exception.FrequencyException;
import com.pongsky.springcloud.exception.HttpException;
import com.pongsky.springcloud.exception.InsertException;
import com.pongsky.springcloud.exception.RemoteCallException;
import com.pongsky.springcloud.exception.UpdateException;
import com.pongsky.springcloud.exception.ValidationException;
import com.pongsky.springcloud.response.GlobalResult;
import com.pongsky.springcloud.response.enums.ResultCode;
import com.pongsky.springcloud.utils.IpUtils;
import com.pongsky.springcloud.utils.trace.CurrentThreadConfig;
import com.pongsky.springcloud.web.request.RequestUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

/**
 * 全局异常处理
 *
 * @author pengsenhao
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper jsonMapper;

    /**
     * 打印堆栈信息最小标识码
     */
    private static final int BOUNDARY = 500;

    /**
     * 校验 param 数据异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return 校验 param 数据异常
     */
    @Nonnull
    @Override
    protected ResponseEntity<Object> handleBindException(@Nonnull BindException ex,
                                                         @Nonnull HttpHeaders headers,
                                                         @Nonnull HttpStatus status,
                                                         @Nonnull WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getRequest();
        Object result = getResult(ResultCode.BindException, getFieldMessages(ex.getBindingResult()),
                ex, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 校验 param 数据异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return 校验 param 数据异常
     */
    @Nonnull
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(@Nonnull MissingServletRequestParameterException ex,
                                                                          @Nonnull HttpHeaders headers,
                                                                          @Nonnull HttpStatus status,
                                                                          @Nonnull WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getRequest();
        Object result = getResult(ResultCode.BindException, ex.getMessage(), ex, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 校验 body 数据异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return 校验 body 数据异常
     */
    @Nonnull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@Nonnull MethodArgumentNotValidException ex,
                                                                  @Nonnull HttpHeaders headers,
                                                                  @Nonnull HttpStatus status,
                                                                  @Nonnull WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getRequest();
        Object result = getResult(ResultCode.MethodArgumentNotValidException,
                getFieldMessages(ex.getBindingResult()), ex, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 获取字段错误信息
     *
     * @param bindingResult bindingResult
     * @return 字段错误信息
     */
    private String getFieldMessages(BindingResult bindingResult) {
        String escapeInterval = "\\.";
        String interval = ".";
        String listStart = "java.util.List<";
        StringBuilder stringBuilder = new StringBuilder("[ ");
        if (bindingResult.getTarget() == null) {
            bindingResult.getFieldErrors().forEach(error -> appendErrorMessage(stringBuilder,
                    error.getField(), error.getDefaultMessage()));
        } else {
            bindingResult.getFieldErrors().forEach(error -> {
                String filedName = error.getField();
                Field field = Arrays.stream(bindingResult.getTarget().getClass().getDeclaredFields())
                        .filter(f -> f.getName().equals(error.getField()))
                        .findFirst()
                        .orElse(null);
                if (field == null) {
                    appendErrorMessage(stringBuilder, filedName, error.getDefaultMessage());
                    return;
                }
                ApiModelProperty meaning = field.getAnnotation(ApiModelProperty.class);
                if (meaning != null) {
                    filedName = meaning.value();
                }
                if (!(filedName.split(escapeInterval).length > 1 && meaning != null)) {
                    appendErrorMessage(stringBuilder, filedName, error.getDefaultMessage());
                    return;
                }
                int i = filedName.lastIndexOf(interval, (filedName.lastIndexOf(interval) - 1)) + 1;
                String[] split = filedName.substring(i).split(escapeInterval);
                filedName = split[0].substring(0, filedName.lastIndexOf("["));
                String typeName = field.getGenericType().getTypeName();
                if (!(typeName.startsWith(listStart))) {
                    appendErrorMessage(stringBuilder, filedName, error.getDefaultMessage());
                    return;
                }
                typeName = typeName.substring(listStart.length(), typeName.lastIndexOf(">"));
                try {
                    Optional<ApiModelProperty> optionalMeaning = Arrays.stream(Class.forName(typeName).getDeclaredFields())
                            .filter(f -> f.getName().equals(split[1]))
                            .map(f -> f.getAnnotation(ApiModelProperty.class))
                            .findFirst();
                    if (optionalMeaning.isPresent()) {
                        filedName += optionalMeaning.get().value();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                appendErrorMessage(stringBuilder, filedName, error.getDefaultMessage());
            });
        }
        return stringBuilder.append("]").toString();
    }

    /**
     * 追加错误字段信息
     *
     * @param stringBuilder 全部错误信息
     * @param filedName     字段名称
     * @param message       字段错误信息
     */
    private void appendErrorMessage(StringBuilder stringBuilder, String filedName, String message) {
        stringBuilder
                .append(filedName)
                .append(" ")
                .append(message)
                .append("; ");
    }

    /**
     * JSON 数据错误异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return JSON 数据错误异常
     */
    @Nonnull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@Nonnull HttpMessageNotReadableException ex,
                                                                  @Nonnull HttpHeaders headers,
                                                                  @Nonnull HttpStatus status,
                                                                  @Nonnull WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getRequest();
        Object result = getResult(ResultCode.HttpMessageNotReadableException, null, ex, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 接口不存在异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return 接口不存在异常
     * @author pengsenhao
     */
    @Nonnull
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(@Nonnull NoHandlerFoundException ex,
                                                                   @Nonnull HttpHeaders headers,
                                                                   @Nonnull HttpStatus status,
                                                                   @Nonnull WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getRequest();
        Object result = getResult(ResultCode.NoHandlerFoundException,
                httpServletRequest.getRequestURI() + " 接口不存在", ex, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 方法不存在异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return 方法不存在异常
     * @author pengsenhao
     */
    @Nonnull
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(@Nonnull HttpRequestMethodNotSupportedException ex,
                                                                         @Nonnull HttpHeaders headers,
                                                                         @Nonnull HttpStatus status,
                                                                         @Nonnull WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getRequest();
        Object result = getResult(ResultCode.HttpRequestMethodNotSupportedException,
                httpServletRequest.getMethod() + " 方法不存在", ex, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 不存在异常
     *
     * @param exception exception
     * @param request   request
     * @return 不存在异常
     */
    @ExceptionHandler(value = DoesNotExistException.class)
    public Object doesNotExistException(DoesNotExistException exception, HttpServletRequest request) {
        return getResult(ResultCode.DoesNotExistException, exception.getLocalizedMessage(), exception, request);
    }

    /**
     * 校验异常
     *
     * @param exception exception
     * @param request   request
     * @return 校验异常
     */
    @ExceptionHandler(value = {ValidationException.class, ConstraintViolationException.class})
    public Object validationException(ValidationException exception, HttpServletRequest request) {
        return getResult(ResultCode.ValidationException, exception.getLocalizedMessage(), exception, request);
    }

    /**
     * HTTP 请求异常
     *
     * @param exception exception
     * @param request   request
     * @return HTTP 请求异常
     */
    @ExceptionHandler(value = HttpException.class)
    public Object httpException(HttpException exception, HttpServletRequest request) {
        return getResult(ResultCode.HttpException, exception.getLocalizedMessage(), exception, request);
    }

    /**
     * 空文件上传异常
     *
     * @param exception exception
     * @param request   request
     * @return 空文件上传异常
     */
    @ExceptionHandler(value = MultipartException.class)
    public Object multipartException(MultipartException exception, HttpServletRequest request) {
        return getResult(ResultCode.MultipartException, null, exception, request);
    }

    /**
     * 最大文件上传大小
     */
    @Value(value = "${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    /**
     * 文件上传大小异常
     *
     * @param exception exception
     * @param request   request
     * @return 文件上传大小异常
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public Object maxUploadSizeExceededException(MaxUploadSizeExceededException exception, HttpServletRequest request) {
        return getResult(ResultCode.MaxUploadSizeExceededException,
                "文件最大 " + maxFileSize + " ，请缩小文件内容后重新上传", exception, request);
    }

    /**
     * 非法参数异常
     *
     * @param exception exception
     * @param request   request
     * @return 非法参数异常
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Object illegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
        return getResult(ResultCode.IllegalArgumentException, null, exception, request);
    }

    /**
     * 存在异常
     *
     * @param exception exception
     * @param request   request
     * @return 存在异常
     */
    @ExceptionHandler(value = ExistException.class)
    public Object existException(ExistException exception, HttpServletRequest request) {
        return getResult(ResultCode.ExistException, null, exception, request);
    }

    /**
     * 频率异常
     *
     * @param exception exception
     * @param request   request
     * @return 频率异常
     */
    @ExceptionHandler(value = FrequencyException.class)
    public Object frequencyException(FrequencyException exception, HttpServletRequest request) {
        return getResult(ResultCode.FrequencyException, null, exception, request);
    }

    /**
     * 保存异常
     *
     * @param exception exception
     * @param request   request
     * @return 保存异常
     */
    @ExceptionHandler(value = InsertException.class)
    public Object insertException(InsertException exception, HttpServletRequest request) {
        return getResult(ResultCode.InsertException, null, exception, request);
    }

    /**
     * 更新异常
     *
     * @param exception exception
     * @param request   request
     * @return 更新异常
     */
    @ExceptionHandler(value = UpdateException.class)
    public Object updateException(UpdateException exception, HttpServletRequest request) {
        return getResult(ResultCode.UpdateException, null, exception, request);
    }

    /**
     * 删除异常
     *
     * @param exception exception
     * @param request   request
     * @return 更新异常
     */
    @ExceptionHandler(value = DeleteException.class)
    public Object deleteException(DeleteException exception, HttpServletRequest request) {
        return getResult(ResultCode.DeleteException, null, exception, request);
    }

    /**
     * 远程调用异常
     *
     * @param exception exception
     * @return 远程调用异常
     */
    @ExceptionHandler(value = RemoteCallException.class)
    public Object remoteCallException(RemoteCallException exception) {
        return exception.getResult();
    }

    /**
     * 系统异常
     *
     * @param exception exception
     * @param request   request
     * @return 系统异常
     */
    @ExceptionHandler(value = Exception.class)
    public Object exception(Exception exception, HttpServletRequest request) {
        return getResult(ResultCode.Exception, null, exception, request);
    }

    /**
     * 封装异常响应体并打印
     *
     * @param resultCode resultCode
     * @param message    message
     * @param exception  exception
     * @param request    request
     * @return 封装异常响应体并打印
     */
    private Object getResult(ResultCode resultCode, String message, Exception exception, HttpServletRequest request) {
        String ip = IpUtils.getIp(request);
        // 可通过 getAttribute 获取自定义注解对 body 数据对特定业务场景进行特殊处理
        GlobalResult<Void> result = GlobalResult.fail(ip, resultCode, request.getRequestURI(), exception.getClass());
        exception = getException(exception, 0);
        if (message != null) {
            result.setMessage(message);
        } else if (result.getMessage() == null) {
            if (exception.getLocalizedMessage() != null) {
                result.setMessage(exception.getLocalizedMessage());
            } else if (exception.getMessage() != null) {
                result.setMessage(exception.getMessage());
            }
        }
        log(exception, request, result);
        return result;
    }

    /**
     * 异常递归次数，防止堆溢出
     */
    private static final int THROWABLE_COUNT = 10;

    /**
     * 获取最底层的异常
     *
     * @param exception 异常
     * @param number    次数
     * @return 获取最底层的异常
     */
    private Exception getException(Exception exception, int number) {
        if (number > THROWABLE_COUNT) {
            return exception;
        }
        if (exception.getCause() != null) {
            return getException(exception, ++number);
        }
        return exception;
    }

    /**
     * 打印日志详细信息
     *
     * @param exception 异常
     * @param request   request
     * @param result    错误响应数据
     */
    private void log(Exception exception, HttpServletRequest request, GlobalResult<Void> result) {
        log.error("");
        log.error("Started exception");
        String ip = IpUtils.getIp(request);
        String userAgent = Optional.ofNullable(request.getHeader(HttpHeaders.USER_AGENT)).orElse("");
        String referer = Optional.ofNullable(request.getHeader(HttpHeaders.REFERER)).orElse("");
        log.error("request header: IP [{}] userAgent [{}] referer [{}]", ip, userAgent, referer);
        if (result.getCode() >= BOUNDARY) {
            log.error("request: methodURL [{}] methodType [{}] params [{}] body [{}]",
                    request.getRequestURI(),
                    request.getMethod(),
                    Optional.ofNullable(request.getQueryString()).orElse(""),
                    Optional.ofNullable(RequestUtils.getBody(request)).orElse(""));
            log.error("exception message: [{}]", result.getMessage());
            Arrays.asList(exception.getStackTrace()).forEach(stackTrace -> log.error(stackTrace.toString()));
        } else {
            log.error("exception message: [{}]", result.getMessage());
        }
        try {
            log.error("response: [{}]", jsonMapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
        }
        log.error("Ended exception");
    }

}
```

#### ResponseResultHandler

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.springcloud.config.SystemConfig;
import com.pongsky.springcloud.response.GlobalResult;
import com.pongsky.springcloud.response.annotation.ResponseResult;
import com.pongsky.springcloud.utils.IpUtils;
import com.pongsky.springcloud.utils.trace.CurrentThreadConfig;
import com.pongsky.springcloud.utils.trace.DiyHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 接口响应体处理器
 *
 * @author pengsenhao
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper jsonMapper;

    @Override
    public boolean supports(@Nonnull MethodParameter returnType,
                            @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @Nonnull MethodParameter returnType,
                                  @Nonnull MediaType selectedContentType,
                                  @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @Nonnull ServerHttpRequest request,
                                  @Nonnull ServerHttpResponse response) {
        String traceId = CurrentThreadConfig.getTraceId();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        Optional.ofNullable(attributes.getResponse())
                .ifPresent(httpServletResponse -> {
                    httpServletResponse.setHeader(DiyHeader.X_HOSTNAME, SystemConfig.getHostName());
                    httpServletResponse.setHeader(DiyHeader.X_INSTANCE_ID, SystemConfig.getInstanceId());
                    httpServletResponse.setHeader(DiyHeader.X_TRACE_ID, traceId);
                    httpServletResponse.setHeader(DiyHeader.X_BACKEND_VERSION, SystemConfig.getVersion());
                });
        HttpServletRequest httpServletRequest = attributes.getRequest();
        // 判断是否已封装好全局响应结果
        if (body instanceof GlobalResult) {
            GlobalResult<?> result = (GlobalResult<?>) body;
            if (result.getIp() == null) {
                result.setIp(IpUtils.getIp(httpServletRequest));
            }
            if (result.getPath() == null) {
                result.setPath(httpServletRequest.getRequestURI());
            }
            return result;
        }
        // 特殊业务场景返回特定格式 直接 return
        // if (httpServletRequest.getAttribute(XXX.class.getSimpleName()) != null) {
        // return body;
        // }
        // 判断是否全局响应数据
        if (httpServletRequest.getAttribute(ResponseResult.class.getSimpleName()) != null) {
            if (body instanceof String) {
                try {
                    return jsonMapper.writeValueAsString(GlobalResult.success(body));
                } catch (JsonProcessingException e) {
                    log.error(e.getLocalizedMessage());
                    return GlobalResult.success(body);
                }
            } else {
                return GlobalResult.success(body);
            }
        }
        return body;
    }

}
```

## `${project}`-service-`${service}`

### 包：`${basePackage}`.config

譬如：com.pongsky.springcloud.config

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
import com.pongsky.springcloud.utils.QuartzUtils;
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

譬如：com.pongsky.springcloud.controller

用于放置所有 controller 接口。

#### 包：`${basePackage}`.controller.api

譬如：com.pongsky.springcloud.controller.api

该包下接口用于第三方调用。

#### 包：`${basePackage}`.controller.console

譬如：com.pongsky.springcloud.controller.console

该包下接口用于前端项目调用。

#### 包：`${basePackage}`.controller.service

譬如：com.pongsky.springcloud.controller.service

该包下接口用于后端项目远程调用。

### 包：`${basePackage}`.job

譬如：com.pongsky.springcloud.job

用于放置所有 job 定时任务调度。

### 包：`${basePackage}`.mapper

譬如：com.pongsky.springcloud.mapper

用于放置所有 Mapper。

### 包：`${basePackage}`.service

譬如：com.pongsky.springcloud.service

用于放置所有 Service。

#### 包：`${basePackage}`.service.impl

譬如：com.pongsky.springcloud.service.impl

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
