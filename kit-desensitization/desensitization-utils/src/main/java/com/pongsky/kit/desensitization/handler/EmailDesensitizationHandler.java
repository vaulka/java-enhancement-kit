package com.pongsky.kit.desensitization.handler;

import com.pongsky.kit.desensitization.utils.DesensitizationUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 邮箱脱敏
 * <p>
 * 示例：k****@vip.qq.com
 *
 * @author pengsenhao
 */
public class EmailDesensitizationHandler implements DesensitizationHandler {

    @Override
    public String exec(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return DesensitizationUtils.desensitization(str, 1, str.lastIndexOf("@"));
    }

}
