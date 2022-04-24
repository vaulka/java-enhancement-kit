import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.excel.utils.ExcelImportUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author pengsenhao
 */
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
