package com.pongsky.kit.desensitization.handler;

import com.pongsky.kit.desensitization.utils.DesensitizationUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 身份证脱敏
 * <p>
 * 示例：350***********2510
 *
 * @author pengsenhao
 */
public class IdCardDesensitizationHandler implements DesensitizationHandler {

    @Override
    public String exec(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return DesensitizationUtils.desensitization(str, 3, 4);
    }

}
