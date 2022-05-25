# spring-boot-starter-springfox 模块说明

> SpringFox Spring Boot Starter 模块

## 功能说明

* 配置文件 添加分组。
* 配置文件 添加文档描述。
* 配置文件 添加全局鉴权参数。

## 约定

1. 支持 Swagger 2 规范。
2. openAPI 3.0 规范尚有缺陷，使用 `@Tag` 注解会出现 API 文档错误。</br> 相关 <a href="https://github.com/springfox/springfox/issues/3668">
   issues</a> PR 至 3.0.1 version，等待官方发版。

## 配置 SpringFox 参数

在 `yml` 配置 SpringDoc 信息，参数如下：

|参数|是否可空|描述|默认值|
|---|---|---|---|
|doc.enabled|true|是否启用|true|
|doc.gateway-route-enabled|true|Swagger 网关路由定位器 是否启用|false|
|doc.title|true|文档标题|API Docs|
|doc.description|true|文档描述||
|doc.version|true|文档版本号||
|doc.requestParameter|true|请求参数信息</br>key: 鉴权参数存放位置</br>value：参数名称列表||
|doc.groups|true|组别列表</br>未填写则会有一个默认分组||

示例如下：

```yml

doc:
  enabled: true
  title: Mihoyo API Docs
  description: 米哈游接口文档
  version: 1.1.0
  request-parameters:
    HEADER:
      - Authorization
      - Token
    QUERY:
      - userId
  groups:
    - display-name: default
      group:


```

## 使用

### 结合 Spring Gateway 实现微服务接口文档分组

```yml

spring:
  main:
    web-application-type: reactive # 响应式 WEB 容器
  cloud:
    gateway:
      globalcors:
        corsConfigurations: # 配置跨域请求
          '[/**]':
            allowedOriginPatterns: "*"
            allowedMethods: "*"
            allowedHeaders: "*"
            allowCredentials: true
      routes:
        - id: swagger
          uri: http://localhost:${server.port}
          predicates:
            - Path=/v3/api-docs/**
          filters:
            - RewritePath=/v3/api-docs/(?<path>.*), /$\{path}/v3/api-docs
        - id: mihoyo
          uri: lb://genshin-adventure-mihoyo
          predicates:
            - Path=/api/mihoyo/**,/console/mihoyo/**
        - id: doc-mihoyo
          uri: lb://genshin-adventure-mihoyo
          predicates:
            - Path=/doc/mihoyo/**
          filters:
            - StripPrefix=2
        - id: druid-mihoyo
          uri: lb://genshin-adventure-mihoyo
          predicates:
            - Path=/druid/mihoyo/**
          filters:
            - StripPrefix=2
            - PrefixPath=/druid
doc:
  groups:
    - display-name: mihoyo
      group: /doc/mihoyo

```

1. 定义 Swagger 资源请求信息，到网关进行处理。

> 将 URL 以 `/v3/api-docs` 开头的请求进行路径重写并重定向。
>
> 路径重写：`/v3/api-docs/**` -> `/**/v3/api-docs`，重定向请求到网关进行后续处理。
>
> 譬如这里定义的 Swagger 组别以及路径为 `/doc/mihoyo`，则完整 URL 为 `/v3/api-docs/doc/mihoyo`，路径重写为 `/doc/mihoyo/v3/api-docs`。
>
> 注：routes 可启用 `doc.gateway-route-enabled` 完成自动配置。

```yml

spring:
  cloud:
    gateway:
      routes:
        - id: swagger
          uri: http://localhost:${server.port}
          predicates:
            - Path=/v3/api-docs/**
          filters:
            - RewritePath=/v3/api-docs/(?<path>.*), /$\{path}/v3/api-docs

doc:
  groups:
    - display-name: mihoyo
      group: /doc/mihoyo

```

2. Swagger 资源请求路由到具体服务。

> 将 URL 以 `/doc/mihoyo` 开头的请求进行路由到 `mihoyo` 模块，并重写路径去掉 `/doc/mihoyo`。
>
> 譬如请求 URL 为 `/doc/mihoyo/v3/api-docs`，重写路径为 `/v3/api-docs`。

```yml

spring:
  cloud:
    gateway:
      routes:
        - id: doc-mihoyo
          uri: lb://genshin-adventure-mihoyo
          predicates:
            - Path=/doc/mihoyo/**
          filters:
            - StripPrefix=2

```

3. 对应服务处理好请求后返回 Swagger 资源信息。
