# spring-boot-starter-web 模块说明

> Web Spring Boot Starter 模块
>
> 该模块依赖
> * [spring-boot-starter-core](../spring-boot-starter-core/README.md) 模块
> * [spring-boot-starter-web-core](../spring-boot-starter-web-core/README.md) 模块
> * [spring-boot-starter-global-response](../spring-boot-starter-global-response/README.md) 模块

## 功能说明

* 统一 Jackson Long to String（防止前端 js 精度丢失）。
* 统一 Jackson Double to String（防止前端 js 精度丢失）。
* 统一 Jackson 时间（Date、LocalDate、LocalTime、LocalDateTime）序列化、反序列化格式。
* 统一 Spring MVC 时间（Date、LocalDate、LocalTime、LocalDateTime）序列化、反序列化格式。
* 定义 `brokerId` 属性，可获取实例ID。
* 详情请查阅：[spring-boot-starter-core](../spring-boot-starter-core/README.md) 模块


* 统一 Controller 日志打印。
* 将调用接口的注解信息存放于 HttpServletRequest Attr。
* 可重复读取 Response Body。
* 详情请查阅：[spring-boot-starter-web-core](../spring-boot-starter-web-core/README.md) 模块


* 统一封装响应数据格式。
* 可自定义成功请求、失败请求、特殊请求响应数据格式。
* 详情请查阅：[spring-boot-starter-global-response](../spring-boot-starter-global-response/README.md) 模块

