import com.alibaba.fastjson.JSON;
import com.pongsky.kit.system.entity.SystemResource;
import com.pongsky.kit.system.utils.SystemResourceUtils;

/**
 * @author pengsenhao
 */
public class SystemResourceUtilsTest {

    public static void main(String[] args) {
        SystemResource resource = SystemResourceUtils.read();
        System.out.println(JSON.toJSONString(resource));

        System.out.println();
    }

}
