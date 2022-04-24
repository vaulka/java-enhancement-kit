# desensitization-annotation 模块说明

> 数据脱敏 annotation 模块

## 功能说明

* 标记哪些字段需要数据脱敏处理。

## 约定

1. 在需要数据脱敏的字段上加 `DesensitizationMark` 注解。
2. 需要自定义实现数据脱敏业务逻辑，请实现 `DesensitizationHandler` 类。

## 使用

> 需配合 [spring-boot-starter-desensitization](../spring-boot-starter-desensitization/README.md) 模块才可食用
