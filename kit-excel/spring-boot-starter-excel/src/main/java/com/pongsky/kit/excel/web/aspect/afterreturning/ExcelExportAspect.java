package com.pongsky.kit.excel.web.aspect.afterreturning;

import com.pongsky.kit.excel.annotation.ExcelExport;
import com.pongsky.kit.excel.enums.ParseType;
import com.pongsky.kit.excel.utils.ExcelExportUtils;
import com.pongsky.kit.excel.utils.ParseResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
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
public class ExcelExportAspect {

    /**
     * 是否导出参数
     * <p>
     * 如果前端 query 参数传此值过来，则会将接口响应数据更改为 excel 文件并导出
     */
    private static final String PARAM = "isExcelExport";

    @SuppressWarnings({"unchecked"})
    @AfterReturning(pointcut = "(@within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController)) " +
            "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)) " +
            "&& @annotation(com.pongsky.kit.excel.annotation.ExcelExport) ", returning = "result")
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
        List<?> content = null;
        List<String> topHeadAttr = null;
        List<String> leftHeadAttr = null;
        Object contentResult = ParseResultUtils.parseFieldValue(excelExport.attr(), result);
        ParseType type = ParseType.getClassType(contentResult);
        if (type == ParseType.LIST) {
            content = (List<?>) contentResult;
        }
        if (StringUtils.isNotBlank(excelExport.topHeadAttr())) {
            Object topHeadAttrResult = ParseResultUtils.parseFieldValue(excelExport.topHeadAttr(), result);
            type = ParseType.getClassType(topHeadAttrResult);
            if (type == ParseType.LIST) {
                topHeadAttr = (List<String>) topHeadAttrResult;
            }
        }
        if (StringUtils.isNotBlank(excelExport.leftHeadAttr())) {
            Object leftHeadAttrResult = ParseResultUtils.parseFieldValue(excelExport.leftHeadAttr(), result);
            type = ParseType.getClassType(leftHeadAttrResult);
            if (type == ParseType.LIST) {
                leftHeadAttr = (List<String>) leftHeadAttrResult;
            }
        }
        if (content == null) {
            return;
        }
        SXSSFWorkbook workbook;
        if (topHeadAttr != null || leftHeadAttr != null) {
            workbook = new ExcelExportUtils(excelExport.value(), topHeadAttr, leftHeadAttr)
                    .export(content, excelExport.sheetName());
        } else {
            workbook = new ExcelExportUtils(excelExport.value())
                    .export(content, excelExport.sheetName());
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
                MessageFormat.format("attachment;filename={0}.xlsx", excelExport.fileName()));
        workbook.write(response.getOutputStream());
        workbook.dispose();
    }

}
