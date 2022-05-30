# spring-boot-starter-captcha-input-char 模块说明

> 输入型字符 验证码 Spring Boot Starter 模块
>
> 该模块依赖
> * [captcha-input-char-utils](../captcha-input-char-utils/README.md) 模块

## 功能说明

* 生成输入型字符 验证码图片 / Base64 字符串。

## 配置验证码参数

在 `yml` 配置验证码信息，参数如下：

|参数|是否可空|描述|默认值|
|---|---|---|---|
|captcha.code-num|true|验证码数量|5|
|captcha.code-width-space|true|每个验证码宽度间距|25|
|captcha.image-height|true|图像高度|35|
|captcha.draw-count|true|干扰线数量|200|
|captcha.line-width|true|干扰线的长度 = 1.414 * LINE_WIDTH|2|

示例如下：

```yml

captcha:
  code-num: 5
  code-width-space: 25
  image-height: 35
  draw-count: 200
  line-width: 2

```

## 使用

可直接注入 `InputCharCaptchaUtils` 进行调用相关方法。

```java

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/captcha", produces = MediaType.APPLICATION_JSON_VALUE)
public class CaptchaController {

    private final InputCharCaptchaUtils captchaUtils;

    @GetMapping
    public String captchaByBase64() throws IOException {
        String code = captchaUtils.createCode();
        return captchaUtils.createImageByBase64(code);
    }

}

```

验证码效果图如下：

![输入型字符验证码](../../../image/输入型字符验证码.jpeg)
