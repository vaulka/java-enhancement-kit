# desensitization-utils 模块说明

> 数据脱敏 utils 模块
>
> 该模块依赖 
> * [desensitization-annotation](../desensitization-annotation/README.md) 模块

## 约定

1. 默认提供以下内置数据脱敏实现
    * 银行卡脱敏 `BankCardDesensitizationHandler`（保留前 4 位、后 4 位）
    * 邮箱脱敏 `EmailDesensitizationHandler`（保留前 2 位、@ 符号以后位数，包含 @ 符号）
    * 身份证脱敏 `IdCardDesensitizationHandler`（保留前 3 位、后 4 位）
    * 姓名脱敏 `NameDesensitizationHandler`（如果姓名两个字，则保留前 1 位，如果姓名超过两个字，则保留前 1 位、后 1 位）
    * 密码脱敏 `PasswordDesensitizationHandler`（保留 0 位）
    * 手机号脱敏 `PhoneDesensitizationHandler`（保留前 3 位、后 4 位）
2. 需要自定义实现数据脱敏业务逻辑，请实现 `DesensitizationHandler` 类。

## 使用

创建数据脱敏实现类，调用 `exec` 方法即可将数据脱敏。

```java

public class DesensitizationUtilsTest {

    public static void main(String[] args) {
        // 银行卡脱敏
        String bankCard = "6217681830027807807";
        System.out.println("bankCard desensitization：" + new BankCardDesensitizationHandler().exec(bankCard));
        System.out.println();

        // 邮箱脱敏
        String email = "kelry@vip.qq.com";
        System.out.println("email desensitization：" + new EmailDesensitizationHandler().exec(email));
        System.out.println();

        // 身份证脱敏
        String idCard = "371257199507072277";
        System.out.println("idCard desensitization:" + new IdCardDesensitizationHandler().exec(idCard));
        System.out.println();

        // 姓名脱敏
        String name = "彭森豪";
        System.out.println("name desensitization:" + new NameDesensitizationHandler().exec(name));
        System.out.println();

        // 密码脱敏
        String password = "123456";
        System.out.println("password desensitization:" + new PasswordDesensitizationHandler().exec(password));
        System.out.println();

        // 手机号脱敏
        String phone = "15753678879";
        System.out.println("phone desensitization:" + new PhoneDesensitizationHandler().exec(phone));

        System.out.println();
    }

}

```

数据脱敏效果图如下：

```log
bankCard desensitization：6217***********7807

email desensitization：ke***@vip.qq.com

idCard desensitization:371***********2277

name desensitization:彭*豪

password desensitization:******

phone desensitization:157****8879
```
