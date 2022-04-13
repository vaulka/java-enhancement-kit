package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.type.parser.utils.ReflectUtils;
import org.apache.poi.ss.usermodel.PictureData;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * InputStream 图片 处理器
 *
 * @author pengsenhao
 **/
public class ExcelInputStreamImageImportHandler implements ExcelImportHandler {

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException, IOException {
        if (obj == null) {
            return;
        }
        PictureData pictureData = (PictureData) obj;
        ReflectUtils.setValue(result, field, new ByteArrayInputStream(pictureData.getData()));
    }

}
