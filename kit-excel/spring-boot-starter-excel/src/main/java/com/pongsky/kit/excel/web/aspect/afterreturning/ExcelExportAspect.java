package com.pongsky.kit.excel.web.aspect.afterreturning;

import com.pongsky.kit.excel.annotation.ExcelExport;
import com.pongsky.kit.excel.enums.ParseResultUtils;
import com.pongsky.kit.excel.enums.ParseType;
import com.pongsky.kit.excel.utils.ExcelExportUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

/**
 * Excel 导出
 *
 * @author pengsenhao
 */
@Aspect
@Component
public class ExcelExportAspect {

    /**
     * 是否导出参数
     * <p>
     * 如果前端 query 参数传此值过来，则会将接口响应数据更改为 excel 文件并导出
     */
    private static final String PARAM = "isExcelExport";

    @AfterReturning(pointcut = "(@within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController)) " +
            "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)) " +
            "&& @annotation(com.pongsky.kit.excel.annotation.ExcelExport)", returning = "result")
    public void exec(JoinPoint point, Object result) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        if (StringUtils.isBlank(request.getParameter(PARAM))) {
            return;
        }
        HttpServletResponse response = attributes.getResponse();
        if (response == null) {
            return;
        }
        ExcelExport excelExport = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(ExcelExport.class);
        result = ParseResultUtils.parseFieldValue(excelExport.attrs(), result);
        ParseType type = ParseType.getClassType(result);
        switch (type) {
            case LIST: {
                List<?> list = (List<?>) result;
                SXSSFWorkbook workbook = new ExcelExportUtils(excelExport.resultClazz())
                        .export(list, excelExport.sheetName());
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition",
                        MessageFormat.format("attachment;filename={0}.xlsx", excelExport.fileName()));
                workbook.write(response.getOutputStream());
                workbook.dispose();
                break;
            }
            default:
                break;
        }
    }

}
