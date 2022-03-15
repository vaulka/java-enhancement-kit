package com.pongsky.kit.desensitization.utils.handler;

import com.pongsky.kit.desensitization.utils.DesensitizationHandler;
import com.pongsky.kit.desensitization.utils.DesensitizationUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 银行卡号脱敏
 * <p>
 * 示例：6217********1201
 *
 * @author pengsenhao
 * @date 2022-03-15 11:57
 */
public class BankCardDesensitizationHandler implements DesensitizationHandler {

    @Override
    public boolean willDoExec(String str) {
        return StringUtils.isNotBlank(str);
    }

    @Override
    public String exec(String str) {
        return DesensitizationUtils.desensitization(str, 4, 4);
    }

}
