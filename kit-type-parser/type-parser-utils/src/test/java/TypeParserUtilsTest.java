import com.pongsky.kit.type.parser.enums.ClassType;

/**
 * @author pengsenhao
 */
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
