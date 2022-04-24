# ip-utils 模块说明

> IP utils 模块

## 功能说明

* 获取 IP。
* 获取 IP 对应的所在地区。

## 使用

```java

public class IpUtilsTest {

    public static void main(String[] args) {
        String address = IpUtils.getAddress("140.243.3.50");
        System.out.println("address：" + address);

        System.out.println();
    }

}

```

效果如下：

```log

address：福建省厦门市 电信

```