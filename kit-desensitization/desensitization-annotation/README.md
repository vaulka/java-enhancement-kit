# desensitization-annotation 模块说明

> 数据脱敏 annotation 模块

## 功能说明

* 标记哪些字段需要数据脱敏处理。

## 约定

1. 在需要数据脱敏的字段上加 `DesensitizationMark` 注解。
2. 需要自定义实现数据脱敏业务逻辑，请实现 `DesensitizationHandler` 类。

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
