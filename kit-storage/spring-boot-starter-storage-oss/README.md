# spring-boot-starter-storage-oss 模块说明

> 云存储 阿里云 OSS Spring Boot Starter 模块
>
> 该模块依赖
> * [spring-boot-storage](../spring-boot-storage/README.md) 模块

## 功能说明

* 实现 阿里云 OSS 创建 bucket。
* 实现 阿里云 OSS 文件上传。

## 配置云存储参数

在 `yml` 配置云存储信息，参数如下：

|参数|是否可空|描述|默认值|
|---|---|---|---|
|aliyun.oss.endpoint|false|endpoint||
|aliyun.oss.bucket|false|bucket||
|aliyun.oss.access-key-id|false|accessKeyId||
|aliyun.oss.secret-access-key|false|secretAccessKey||

示例如下：

```yml

aliyun:
  oss:
    endpoint: endpoint
    bucket: bucket
    access-key: accessKeyId
    secret-access-key: secretAccessKey

```

## 使用

```java

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadController {

    private final StorageUtils storageUtils;

    @PostMapping
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String fileName = storageUtils.upload(file.getOriginalFilename(), file.getContentType(), file.getInputStream());
        return fileName;
    }

}

```