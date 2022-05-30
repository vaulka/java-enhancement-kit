# spring-boot-starter-web-core 模块说明

> Web Core Spring Boot Starter 模块
>
> 该模块依赖
> * [ip-utils](../../kit-ip/ip-utils/README.md) 模块
> * [validation-utils](../../kit-validation/validation-utils/README.md) 模块
> * [spring-boot-common](../spring-boot-common/README.md) 模块

## 功能说明

* 统一 Controller 日志打印。
* 将调用接口的注解信息存放于 HttpServletRequest Attr。
* Request Body 数据多次读取。
* 跨域访问。
* XSS 防御。