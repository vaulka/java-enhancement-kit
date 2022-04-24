# spring-boot-starter-captcha-input-math 模块说明

> 输入型算数 验证码 Spring Boot Starter 模块
>
> 该模块依赖 
> * [captcha-input-math-utils](../captcha-input-math-utils/README.md) 模块

## 配置验证码参数

在 `yml` 配置验证码信息，参数如下：

|参数|是否可空|描述|默认值|
|---|---|---|---|
|captcha.min-code|true|最小验证码数值|10|
|captcha.max-code|true|最大验证码数值|99|
|captcha.code-width-space|true|每个验证码宽度间距|25|
|captcha.image-height|true|图像高度|35|
|captcha.draw-count|true|干扰线数量|200|
|captcha.line-width|true|干扰线的长度 = 1.414 * LINE_WIDTH|2|

示例如下：

```yml

captcha:
  min-code: 10
  max-code: 99
  code-width-space: 25
  image-height: 35
  draw-count: 200
  line-width: 2

```

## 使用

可直接注入 `CaptchaUtils` 进行调用相关方法。

```java

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/captcha", produces = MediaType.APPLICATION_JSON_VALUE)
public class CaptchaController {

    private final CaptchaUtils captchaUtils;

    @GetMapping
    public String captchaByBase64() throws IOException {
        int code = captchaUtils.createCode();
        return captchaUtils.createImageByBase64(code);
    }

}

```

验证码效果图如下：

![输入型算数验证码](../../../image/输入型算数验证码.jpeg)
