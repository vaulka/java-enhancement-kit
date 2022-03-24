package com.pongsky.kit.desensitization.handler;

import com.pongsky.kit.desensitization.handler.DesensitizationHandler;
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
    public boolean willDoExec(String str) {
        return StringUtils.isNotBlank(str);
    }

    @Override
    public String exec(String str) {
        return DesensitizationUtils.desensitization(str, 3, 4);
    }

}
