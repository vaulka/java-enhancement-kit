# spring-boot-starter-aliyun-sms 模块说明

> 阿里云 SMS Spring Boot Starter 模块
>
> 该模块依赖
> * [aliyun-sms-utils](../aliyun-sms-utils/README.md) 模块

## 功能说明

* 实现 阿里云 SMS 发送短信。

## 配置 SMS 参数

在 `yml` 配置 SMS 信息，参数如下：

|参数|是否可空|描述|默认值|
|---|---|---|---|
|aliyun.oss.region-id|false|regionId||
|aliyun.oss.access-key-id|false|accessKeyId||
|aliyun.oss.access-key-secret|false|accessKeySecret||

示例如下：

```yml

aliyun:
  oss:
    region-id: regionId
    access-key-id: accessKeyId
    access-key-secret: accessKeySecret

```

## 使用

### 发送短信

```java

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/sms", produces = MediaType.APPLICATION_JSON_VALUE)
public class StorageController {

    private final AliYunSmsUtils aliyunSmsUtils;

    public static class TestTemplate extends SmsTemplate<TestTemplateParam> {

        public TestTemplate(TestTemplateParam param) {
            super(param);
        }

        @Override
        public String getCode() {
            return "SMS_154950909";
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
        aliyunSmsUtils.sendSms("阿里云短信测试", new TestTemplate(new TestTemplateParam("123456")), "1515***5510");
    }

}

```
