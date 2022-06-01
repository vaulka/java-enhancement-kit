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

### 简单上传

```java

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/storage", produces = MediaType.APPLICATION_JSON_VALUE)
public class StorageController {

    private final AliYunOssUtils aliYunOssUtils;

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String url = aliYunOssUtils.upload(file.getOriginalFilename(), file.getInputStream());
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

    private final AliYunOssUtils aliYunOssUtils;

    @PostMapping("init-part-upload")
    public String initPartUpload(@RequestParam String fileName) {
        String uploadId = aliYunOssUtils.initPartUpload(fileName);
        return uploadId;
    }

    @PostMapping("part-upload")
    public void partUpload(@RequestParam MultipartFile file,
                           @RequestParam String uploadId,
                           @RequestParam Integer partNumber) throws IOException {
        aliYunOssUtils.partUpload(uploadId, partNumber, (int) file.getSize(), file.getName(), new BufferedInputStream(file.getInputStream()));
    }

    @PostMapping("complete-part-upload")
    public String completePartUpload(@RequestParam String uploadId,
                                     @RequestParam String fileName) {
        List<PartETag> parts = aliYunOssUtils.listPart(uploadId, fileName);
        String url = aliYunOssUtils.completePartUpload(uploadId, fileName, parts);
        return url;
    }

}

```
