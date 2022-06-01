import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.kit.system.entity.SystemResource;
import com.pongsky.kit.system.utils.SystemResourceUtils;

/**
 * @author pengsenhao
 */
public class SystemResourceUtilsTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {
        SystemResource resource = SystemResourceUtils.read();
        System.out.println(OBJECT_MAPPER.writeValueAsString(resource));

        System.out.println();
    }

}
