# spring-boot-starter-trace 模块说明

> 链路 Spring Boot Starter 模块
>
> 该模块依赖
> * [spring-boot-starter-common](../spring-boot-starter-common/README.md) 模块

## 功能说明

* 响应头返回 `链路ID`、`HostName`、`实例ID`、`后端版本号`。
* 异步线程池记录链路信息。
* Feign 远程调用记录链路信息。
* Quartz 任务调度记录链路信息。

## 配置应用参数

在 `yml` 配置应用信息，参数如下：

|参数|是否可空|描述|默认值|
|---|---|---|---|
|application.name|true|应用名称||
|application.version|true|应用版本号||
|application.active|true|环境||
|application.host-name|true|HostName|系统的 HostName|
|application.instance-id|true|实例ID|自生成的 brokerId|

示例如下：

```yml

application:
   name: mihoyo
   version: 1.0.3
   host-name: CentOS
   instance-id: ${brokerId}

```
