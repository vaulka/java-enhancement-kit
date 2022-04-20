package com.pongsky.kit.excel.autoconfigure;

import com.pongsky.kit.excel.web.aspect.afterreturning.ExcelExportAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Excel 自动装配
 *
 * @author pengsenhao
 */
@Configuration(proxyBeanMethods = false)
@Import({ExcelExportAspect.class})
public class ExcelAutoConfiguration {


}
