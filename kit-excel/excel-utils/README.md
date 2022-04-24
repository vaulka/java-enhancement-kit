# excel-utils 模块说明

> Excel utils 模块

## 功能说明

* 将 Excel 文件导入到数据列表中（导入）。
* 将数据列表转成 Excel 文件并导出（导出）。
* 支持自定义导入/导出解析数据处理器。
* 提供内置的导入/导出数据处理器。
    * 导入
        * String
        * Boolean
        * Byte
        * Character
        * Double
        * Float
        * Integer
        * Long
        * Short
        * BigDecimal
        * BigInteger
        * Month
        * Integer Month
        * DayOfWeek
        * Integer Week
        * Date（yyyy-MM-dd HH:mm:ss format）
        * LocalDate（yyyy-MM-dd format）
        * LocalTime（HH:mm:ss format）
        * LocalDateTime（yyyy-MM-dd HH:mm:ss format）
        * Byte to Image
        * File to Image
        * InputStream to Image
        * Url to Image
    * 导出
        * String
        * Boolean
        * Byte
        * Character
        * Double
        * Float
        * Integer
        * Long
        * Short
        * BigDecimal
        * BigInteger
        * Month
        * Integer Month
        * DayOfWeek
        * Integer Week
        * Date（yyyy-MM-dd HH:mm:ss format）
        * LocalDate（yyyy-MM-dd format）
        * LocalTime（HH:mm:ss format）
        * LocalDateTime（yyyy-MM-dd HH:mm:ss format）
        * Byte Image
        * File Image
        * InputStream Image
* 导出支持额外功能如下：
    * 多层级属性获取
    * 动态标题
    * 行高自适应
    * 列宽自适应
    * 自定义左部标题
    * 自定义顶部标题
    * 标题相同自动合并
    * 标题、内容 自定义样式
    * 标题添加超链接、批注
    * 保护单元格

## 约定

1. 在需要导入/导出的字段上加 `ExcelProperty` or `ExcelPropertys` 注解。

## 使用

### 导出

```java

public class ExcelExportUtilsTest {

    public static class User {

        @ExcelProperty(topHeads = "姓名")
        private String name;
        @ExcelProperty(topHeads = "年龄")
        private Integer age;
        @ExcelProperty(topHeads = "描述")
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

    public static void main(String[] args) throws IOException {
        User userA = new User();
        userA.setName("张杰");
        userA.setAge(18);
        userA.setDesc("张杰（Jason Zhang），1982年12月20日出生于四川省成都市，中国流行男歌手。2004年参加歌唱类选秀《我型我秀》，获得全国总冠军并出道。");

        User userB = new User();
        userB.setName("周杰伦");
        userB.setAge(19);
        userB.setDesc("周杰伦（Jay Chou），1979年1月18日出生于台湾省新北市，祖籍福建省泉州市永春县，中国台湾流行乐男歌手、音乐人、演员、导演、编剧，毕业于淡江中学。");
        List<User> users = Arrays.asList(userA, userB);

        ExcelExportUtils utils = new ExcelExportUtils(User.class);
        // 生成 Excel 文件
        SXSSFWorkbook workbook = utils.export(users, "学生信息");

        // 导出 Excel 文件
        FileOutputStream outputStream = new FileOutputStream("/Users/pengsenhao/Downloads/user.xlsx");
        workbook.write(outputStream);
        workbook.dispose();

    }

}

```

效果如下：

![导出](../../image/导出.png)

### 导入

```java

public class ExcelImportUtilsTest {

    public static class User {

        @ExcelProperty(topHeads = "姓名")
        private String name;
        @ExcelProperty(topHeads = "年龄")
        private Integer age;
        @ExcelProperty(topHeads = "描述")
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

    public static void main(String[] args) throws IOException, ReflectiveOperationException {
        ExcelImportUtils<User> utils = new ExcelImportUtils<>(User.class);
        File file = new File("/Users/pengsenhao/Downloads/user.xlsx");
        // 导入 Excel 文件
        List<User> users = utils.read(file, "学生信息");

        System.out.println(users);
    }

}

```

## 强大的对手

* [EasyExcel](https://www.yuque.com/easyexcel/doc/easyexcel)
