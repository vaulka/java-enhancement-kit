# spring-boot-starter-global-response 模块说明

> 全局响应 Spring Boot Starter 模块
>
> 该模块依赖
> * [validation-utils](../../kit-validation/validation-utils/README.md) 模块
> * [spring-boot-common](../spring-boot-common/README.md) 模块
> * [spring-boot-starter-web-core](../spring-boot-starter-web-core/README.md) 模块

## 功能说明

* 统一封装响应数据格式。
* 可自定义成功请求、失败请求、特殊请求响应数据格式。

## 约定

1. 提供内置统一的响应数据格式以及编码定义。
2. 提供内置统一响应数据格式的失败处理器。
    * 访问权限异常处理器（AccessDeniedExceptionFailProcessor）
    * request body 数据校验异常 / param 数据校验异常 处理器（BindExceptionFailProcessor）
    * 断路异常处理器（CircuitBreakerExceptionFailProcessor）
    * 默认【失败】全局响应处理器（DefaultFailProcessor）
    * 删除异常处理器（DeleteExceptionFailProcessor）
    * 不存在异常处理器（DoesNotExistExceptionFailProcessor）
    * 存在异常处理器（ExistExceptionFailProcessor）
    * 频率异常处理器（FrequencyExceptionFailProcessor）
    * HTTP 请求异常处理器（HttpExceptionFailProcessor）
    * request body 数据转换异常处理器（HttpMessageNotReadableExceptionFailProcessor）
    * 方法不存在异常处理器（HttpRequestMethodNotSupportedExceptionFailProcessor）
    * 非法参数异常处理器（IllegalArgumentExceptionFailProcessor）
    * 保存异常处理器（InsertExceptionFailProcessor）
    * 文件上传大小异常处理器（MaxUploadSizeExceededExceptionFailProcessor）
    * param 数据校验异常处理器（MissingServletRequestParameterExceptionFailProcessor）
    * 空文件上传异常处理器（MultipartExceptionFailProcessor）
    * 接口不存在异常处理器（NoHandlerFoundExceptionFailProcessor）
    * 空指针异常处理器（NullPointerExceptionFailProcessor）
    * 运行时异常处理器（RuntimeExceptionFailProcessor）
    * 数据类型转换异常处理器（TypeMismatchExceptionFailProcessor）
    * 修改异常处理器（UpdateExceptionFailProcessor）
    * 校验异常处理器（ValidationExceptionFailProcessor）
3. 提供内置统一响应数据格式的成功处理器。
    * 默认【成功】全局响应处理器（DefaultSuccessProcessor）
4. 提供内置的忽略响应数据格式处理器
    * 不拦截 Springfox ApiResourceController 相关资源请求（ApiResourceControllerSupportsReturnTypeProcessor）
    * 不拦截 Springfox OpenApiControllerWebMvc 相关资源请求（OpenApiControllerWebMvcSupportsReturnTypeProcessor）
    * 不拦截 Springfox Swagger2ControllerWebMvc 相关资源请求（Swagger2ControllerWebMvcSupportsReturnTypeProcessor）
    * 不拦截 Actuator 相关资源请求（ActuatorReturnTypeProcessor）
5. 使用内置的全局响应处理，需在接口或类加上 `ResponseResult` 注解。
6. 自定义【失败处理器】、【失败环绕处理器】、【成功处理器】、【成功环绕处理器】、【不拦截请求处理器】等处理器时，需将该类注册成 `Bean`，后续业务处理才方可识别到。
