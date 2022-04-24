# spring-boot-starter-dynamic-datasource 模块说明

> 动态数据源 Spring Boot Starter 模块

## 约定

1. 目前只适配了 `MySQL` 数据库动态数据源。
2. 数据库连接池采用 [druid](https://github.com/alibaba/druid/wiki/%E6%96%B0%E6%89%8B%E6%8C%87%E5%8D%97) ，所有数据源将复用 `druid` 连接池配置。
3. 原有 `spring.datasource` 数据库配置当作 `default` 默认数据源，当未填写动态数据源或填写错误时，将采用该数据源。

## 配置动态数据源

在 `yml` 配置多数据源信息，参数如下：

|参数|是否可空|描述|
|---|---|---|
|spring.multi-datasets.[key]|false|数据源名称 <br> 可进行解析组名，组名根据 - 符号间隔开，取 - 符号前的字符串。，取 - 符号前的字符串。<br> 譬如：master-1，名称为 master-1，组名为 master|
|spring.multi-datasets.[key].url|false|数据库 URL|
|spring.multi-datasets.[key].driver-class-name|true|数据库驱动程序 <br> 未填写则复用默认配置数据库配置信息|
|spring.multi-datasets.[key].username|true|数据库账号 <br> 未填写则复用默认配置数据库配置信息|
|spring.multi-datasets.[key].password|true|数据库密码 <br> 未填写则复用默认配置数据库配置信息|

> WARN
>
> 1. 数据源 URL 禁止重复。 
> 2. 数据源名称禁止填写 `default`，因为该数据源名称为默认数据源 `spring.datasource` 的配置。

示例如下：

```yml

spring:
   multi-datasets:
      master-1:
         url: jdbc:mysql://localhost:3306/xxx?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&a=1
         driver-class-name: com.mysql.cj.jdbc.Driver
         username: xxx
         password: xxx
      master-2:
         url: jdbc:mysql://localhost:3306/yyy?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&a=2
      master-3:
         url: jdbc:mysql://localhost:3306/zzz?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&a=3
      back:
         url: jdbc:mysql://localhost:3306/xyz?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&a=4

```

## 使用

1. 在需要切换数据源的方法上加 `DataSource` 注解，即可切换数据源，当未填写动态数据源或填写错误时，将采用 `default` 数据源。
2. `DataSource` 动态数据源名称可填写具体名称或者组名，如填写组名，则将随机选择数据源进行分配。

```java

@Service
@RequiredArgsConstructor
public class AccountServiceImpl {

    @DataSource
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    @Override
    public void test() {
        // query datasource

    }

    @DataSource("master")
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    @Override
    public void query() {
        // query datasource

    }

    @DataSource("master-1")
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    @Override
    public void detail() {
        // query datasource

    }

}

```

## 强大的对手

* [多数据源 - MyBatis-Plus](https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter#https://gitee.com/link?target=https%3A%2F%2Fwww.kancloud.cn%2Ftracy5546%2Fdynamic-datasource%2F2264611)
