package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import org.apache.poi.ss.usermodel.PictureData;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Byte 图片 处理器
 *
 * @author pengsenhao
 **/
public class ExcelByteImageImportHandler implements ExcelImportHandler {

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException, IOException {
        if (obj == null) {
            return;
        }
        PictureData pictureData = (PictureData) obj;
        this.setValue(result, field, pictureData.getData());
    }

}
