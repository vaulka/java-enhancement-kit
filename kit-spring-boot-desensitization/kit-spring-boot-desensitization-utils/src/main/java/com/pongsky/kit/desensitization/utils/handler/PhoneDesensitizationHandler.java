package com.pongsky.kit.desensitization.utils.handler;

import com.pongsky.kit.desensitization.annotation.DesensitizationHandler;
import com.pongsky.kit.desensitization.utils.DesensitizationUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 手机号脱敏
 * <p>
 * 示例：151***5510
 *
 * @author pengsenhao
 * @date 2022-03-15 11:57
 */
public class PhoneDesensitizationHandler implements DesensitizationHandler {

    @Override
    public boolean willDoExec(String str) {
        return StringUtils.isNotBlank(str);
    }

    @Override
    public String exec(String str) {
        return DesensitizationUtils.desensitization(str, 3, 4);
    }

}
