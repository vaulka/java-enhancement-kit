# validation-utils 模块说明

> 校验 utils 模块

## 功能说明

* javax validation 校验。
* 美化校验内容，支持将字段自定义名称显示。
* 提供内置组别。
* 提供内置校验规则
    * 开始时间与结束时间校验
    * 最小数值与最大数值校验

## 使用

```java

public class ValidationUtilsTest {

  public static class User {

    @Property("姓名")
    private String name;
    @Property("年龄")
    @Null
    @Range(min = 1,max = 2)
    private Integer age;
    @Property("描述")
    @Length(min = 100)
    private String desc;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getAge() {
      return age;
    }

    public void setAge(Integer age) {
      this.age = age;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }
  }

  public static void main(String[] args) {
    User user = new User();
    user.setName("张杰");
    user.setAge(18);
    user.setDesc("张杰（Jason Zhang），1982年12月20日出生于四川省成都市，中国流行男歌手。2004年参加歌唱类选秀《我型我秀》，获得全国总冠军并出道。");

    String message = ValidationUtils.validation(user);
    System.out.println(message);

    System.out.println();
  }

}

```

效果如下：

```log

参数校验失败，一共有 3 处错误，详情如下： 年龄 必须为 null; 年龄 需要在 1 和 2 之间; 描述 长度需要在 100 和 2147483647 之间;

```