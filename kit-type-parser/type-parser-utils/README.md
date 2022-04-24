# type-parser-utils 模块说明

> 类型解析 utils 模块

## 功能说明

* 类类型解析。
* 字段类型解析。
* 获取数组的范型类。
* 获取类的所有父类。
* 获取类的所有字段。
* 反射设置值、获取值。

## 使用

```java

public class TypeParserUtilsTest {

    public static class User {
    }

    public static void main(String[] args) {
        User user = new User();
        ClassType classType = ClassType.getType(user);
        System.out.println(classType);

        System.out.println();
    }

}

```

效果如下：

```log

OBJECT

```