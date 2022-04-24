# spring-boot-starter-storage-minio 模块说明

> 云存储 MinIO Spring Boot Starter 模块
>
> 该模块依赖 
> * [storage-annotation](../storage-annotation/README.md) 模块

## 功能说明

* 实现 MinIO 创建 bucket。
* 实现 MinIO 文件上传。

## 配置云存储参数

在 `yml` 配置云存储信息，参数如下：

|参数|是否可空|描述|默认值|
|---|---|---|---|
|minio.endpoint|false|endpoint||
|minio.bucket|false|bucket||
|minio.access-key|false|accessKey||
|minio.secret-key|false|secretKey||

示例如下：

```yml

minio:
  endpoint: endpoint
  bucket: bucket
  access-key: accessKey
  secret-key: secretKey

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