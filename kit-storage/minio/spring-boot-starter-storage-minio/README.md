# spring-boot-starter-storage-minio 模块说明

> 云存储 MinIO Spring Boot Starter 模块
>
> 该模块依赖
> * [spring-boot-storage](../../spring-boot-storage/README.md) 模块
> * [storage-minio-utils](../storage-minio-utils/README.md) 模块

## 功能说明

* 实现 MinIO 创建 bucket。
* 实现 MinIO 简单上传。
* 实现 MinIO 分片上传。

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

### 简单上传

```java

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/storage", produces = MediaType.APPLICATION_JSON_VALUE)
public class StorageController {

    private final MinIoUtils minIoUtils;

    @PostMapping
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String url = minIoUtils.upload(file.getOriginalFilename(), file.getContentType(), file.getInputStream());
        return url;
    }

}

```

### 分片上传

流程如下：

1. 前端调用 `init-part-upload` 接口，获取到 `uploadId`。
2. 前端将原始文件进行分片处理，将分片后的文件调用 `part-upload` 接口进行上传。
3. 所有文件都上传完毕后，调用 `complete-part-upload` 接口进行合并分片，并返回文件访问路径。

```java

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/storage", produces = MediaType.APPLICATION_JSON_VALUE)
public class StorageController {

    private final MinIoUtils minIoUtils;

    @PostMapping("init-part-upload")
    public String initPartUpload(@RequestParam String fileName) {
        String uploadId = minIoUtils.initPartUpload(fileName);
        return uploadId;
    }

    @PostMapping("part-upload")
    public void partUpload(@RequestParam MultipartFile file,
                           @RequestParam String uploadId,
                           @RequestParam Integer partNumber) throws IOException {
        minIoUtils.partUpload(uploadId, partNumber, (int) file.getSize(), file.getName(), file.getInputStream());
    }

    @PostMapping("complete-part-upload")
    public String completePartUpload(@RequestParam String uploadId,
                                     @RequestParam String fileName) {
        List<Part> parts = minIoUtils.listPart(uploadId, fileName);
        String url = minIoUtils.completePartUpload(uploadId, fileName, parts);
        return url;
    }

}

```
