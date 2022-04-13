package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.type.parser.utils.ReflectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.PictureData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.MessageFormat;

/**
 * File 图片 处理器
 *
 * @author pengsenhao
 **/
public class ExcelFileImageImportHandler implements ExcelImportHandler {

    private static final String FILE_PATH_PATH = "{0}/temp";

    private static final String FILE_NAME_PATH = "{0}.{1}";

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException, IOException {
        if (obj == null) {
            return;
        }
        PictureData pictureData = (PictureData) obj;
        String dirName = MessageFormat.format(FILE_PATH_PATH, new File("").getAbsolutePath());
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = MessageFormat.format(FILE_NAME_PATH, RandomStringUtils.randomAlphanumeric(10), pictureData.suggestFileExtension());
        File file = new File(dirName, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(pictureData.getData());
        }
        ReflectUtils.setValue(result, field, file);
    }

}
