import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.excel.utils.ExcelExportUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author pengsenhao
 */
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
