import com.pongsky.kit.ip.utils.IpUtils;

/**
 * @author pengsenhao
 */
public class IpUtilsTest {

    public static void main(String[] args) {
        String address = IpUtils.getAddress("140.243.3.50");
        System.out.println("address：" + address);

        System.out.println();
    }

}
