package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.excel.entity.ExcelExportInfo;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * File 图片 处理器
 *
 * @author pengsenhao
 **/
public class ExcelFileImageHandler extends ExcelBufferedImageHandler {

    @Override
    public void exec(Field field, ExcelProperty excelProperty, Object obj, ExcelExportInfo info) throws IOException {
        File imageFile = (File) obj;
        if (imageFile == null || !imageFile.exists() || imageFile.isDirectory() || imageFile.length() == 0) {
            return;
        }
        BufferedImage bufferedImage;
        String suffix;
        try (ImageInputStream inputStream = new FileImageInputStream(imageFile)) {
            suffix = this.getSuffix(inputStream);
            bufferedImage = ImageIO.read(imageFile);
        }
        this.setBufferedImage(bufferedImage);
        this.setSuffix(suffix);
        super.exec(field, excelProperty, imageFile, info);
    }

}
