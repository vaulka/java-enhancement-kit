import com.pongsky.kit.validation.annotation.Property;
import com.pongsky.kit.validation.utils.ValidationUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Null;

/**
 * @author pengsenhao
 */
public class ValidationUtilsTest {

    public static class User {

        @Property("姓名")
        private String name;
        @Property("年龄")
        @Null
        @Range(min = 1, max = 2)
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
