# spring-boot-starter-tencent-sms 模块说明

> 腾讯云 SMS Spring Boot Starter 模块
>
> 该模块依赖
> * [tencent-sms-utils](../tencent-sms-utils/README.md) 模块

## 功能说明

* 实现 腾讯云 SMS 发送短信。

## 配置 SMS 参数

在 `yml` 配置 SMS 信息，参数如下：

|参数|是否可空|描述|默认值|
|---|---|---|---|
|tencent.sms.region|false|region||
|tencent.sms.sdk-app-id|false|sdkAppId||
|tencent.sms.secret-id|false|secretId||
|tencent.sms.secret-key|false|secretKey||

示例如下：

```yml

tencent:
  sms:
    region: region
    sdk-app-id: sdkAppId
    secret-id: secretId
    secret-key: secretKey

```

## 使用

### 发送短信

```java

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/sms", produces = MediaType.APPLICATION_JSON_VALUE)
public class StorageController {

    private final TenCentSmsUtils tenCentSmsUtils;

    public static class TestTemplate extends TenCentSmsTemplate {

        public TestTemplate(List<String> param) {
            super(param);
        }

        @Override
        public String getId() {
            return "123";
        }

        @Override
        public String format() {
            return "您正在使用阿里云短信测试服务，体验验证码是：${code}，如非本人操作，请忽略本短信！";
        }

    }

    public static class TestTemplateParam {

        public TestTemplateParam(String code) {
            this.code = code;
        }

        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    @PostMapping("/send")
    public void send() {
        tenCentSmsUtils.sendSms("xxx", new TestTemplate(List.of("123456","5")), "1515***5510");
    }
}

```
