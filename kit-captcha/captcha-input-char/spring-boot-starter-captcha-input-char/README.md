# spring-boot-starter-captcha-input-char 模块说明

> 输入型字符 验证码 Spring Boot Starter 模块
>
> 该模块依赖
> * [captcha-input-char-utils](../captcha-input-char-utils/README.md) 模块

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
        String code = captchaUtils.createCode();
        return captchaUtils.createImageByBase64(code);
    }

}

```

验证码效果图如下：

![输入型字符验证码](../../../image/输入型字符验证码.jpeg)
