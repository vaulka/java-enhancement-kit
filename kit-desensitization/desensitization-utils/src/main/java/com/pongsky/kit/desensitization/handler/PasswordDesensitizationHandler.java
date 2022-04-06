package com.pongsky.kit.desensitization.handler;

import com.pongsky.kit.desensitization.utils.DesensitizationUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 密码脱敏
 * <p>
 * 示例：******
 *
 * @author pengsenhao
 */
public class PasswordDesensitizationHandler implements DesensitizationHandler {

    @Override
    public String exec(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return DesensitizationUtils.desensitization(str, 0, 0);
    }

}
