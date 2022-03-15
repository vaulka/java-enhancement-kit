package com.pongsky.kit.desensitization.utils.handler;

import com.pongsky.kit.desensitization.annotation.DesensitizationHandler;
import com.pongsky.kit.desensitization.utils.DesensitizationUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 名称脱敏
 * <p>
 * 示例：彭*
 * 示例：彭*豪
 * 示例：彭**豪
 *
 * @author pengsenhao
 * @date 2022-03-15 11:57
 */
public class NameDesensitizationHandler implements DesensitizationHandler {

    @Override
    public boolean willDoExec(String str) {
        return StringUtils.isNotBlank(str);
    }

    @Override
    public String exec(String str) {
        return str.length() == 2
                ? DesensitizationUtils.desensitization(str, 1, 0)
                : DesensitizationUtils.desensitization(str, 1, 1);
    }

}
