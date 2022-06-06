# spring-boot-storage 模块说明

> 云存储 Spring Boot 模块
>
> 该模块依赖 
> * [type-parser-utils](../../kit-type-parser/type-parser-utils/README.md) 模块
> * [storage-annotation](../storage-annotation/README.md) 模块
> * [spring-boot-starter-common](../../kit-spring-boot/spring-boot-starter-common/README.md) 模块

## 功能说明

* 云存储资源标记，定义哪些字段需要添加文件基地址前缀。
* 上传空文件校验。
* 云存储资源路径可自动添加基地址前缀。

## 配置云存储参数

在 `yml` 配置云存储信息，参数如下：

|参数|是否可空|描述|默认值|
|---|---|---|---|
|storage.base-uri|true|文件基地址||
|storage.is-enable-resource-mark|true|是否启用补充 uri|true|

示例如下：

```yml

storage:
  base-uri: https://baidu.com
  is-enable-resource-mark: true

```

## 约定

1. 在需要自动添加云存储资源路径前缀的字段添加 `StorageResourceMark` 注解。
