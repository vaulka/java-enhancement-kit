package com.pongsky.kit.desensitization.handler;

import com.pongsky.kit.desensitization.utils.DesensitizationUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 银行卡号脱敏
 * <p>
 * 示例：6217********1201
 *
 * @author pengsenhao
 */
public class BankCardDesensitizationHandler implements DesensitizationHandler {

    @Override
    public String exec(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return DesensitizationUtils.desensitization(str, 4, 4);
    }

}
