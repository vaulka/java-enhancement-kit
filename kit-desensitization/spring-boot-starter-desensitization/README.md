# spring-boot-starter-desensitization 模块说明

> 数据脱敏 Spring Boot Starter 模块
>
> 该模块依赖
> * [desensitization-annotation](../desensitization-annotation/README.md) 模块
> * [desensitization-utils](../desensitization-utils/README.md) 模块

## 约定

1. 在需要数据脱敏的字段上加 `DesensitizationMark` 注解。
2. 默认提供以下内置数据脱敏实现
    * 银行卡脱敏 `BankCardDesensitizationHandler`（保留前 4 位、后 4 位）
    * 邮箱脱敏 `EmailDesensitizationHandler`（保留前 2 位、@ 符号以后位数，包含 @ 符号）
    * 身份证脱敏 `IdCardDesensitizationHandler`（保留前 3 位、后 4 位）
    * 姓名脱敏 `NameDesensitizationHandler`（如果姓名两个字，则保留前 1 位，如果姓名超过两个字，则保留前 1 位、后 1 位）
    * 密码脱敏 `PasswordDesensitizationHandler`（保留 0 位）
    * 手机号脱敏 `PhoneDesensitizationHandler`（保留前 3 位、后 4 位）
3. 需要自定义实现数据脱敏业务逻辑，请实现 `DesensitizationHandler` 类。

## 使用

在需要数据脱敏的字段上加 `DesensitizationMark` 注解，响应数据即可实现数据脱敏。

```java

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/desensitization", produces = MediaType.APPLICATION_JSON_VALUE)
public class DesensitizationController {

   @GetMapping
   public User desensitization() {
      return new User()
              .setBankCard("6217681830027807807")
              .setEmail("kelry@vip.qq.com")
              .setIdCard("371257199507072277")
              .setName("彭森豪")
              .setPassword("123456")
              .setPhone("15753678879");
   }

   @Data
   @Accessors(chain = true)
   @EqualsAndHashCode(callSuper = false)
   public static class User {
      @DesensitizationMark(handler = BankCardDesensitizationHandler.class)
      private String bankCard;
      @DesensitizationMark(handler = EmailDesensitizationHandler.class)
      private String email;
      @DesensitizationMark(handler = IdCardDesensitizationHandler.class)
      private String idCard;
      @DesensitizationMark(handler = NameDesensitizationHandler.class)
      private String name;
      @DesensitizationMark(handler = PasswordDesensitizationHandler.class)
      private String password;
      @DesensitizationMark(handler = PhoneDesensitizationHandler.class)
      private String phone;
   }

}

```

数据脱敏效果图如下：

```json
{
    "bankCard": "6217***********7807",
    "email": "k**********q.com",
    "idCard": "371***********2277",
    "name": "彭*豪",
    "password": "******",
    "phone": "157****8879"
}
```
