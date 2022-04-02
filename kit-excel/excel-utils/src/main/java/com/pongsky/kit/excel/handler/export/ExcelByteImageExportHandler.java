package com.pongsky.kit.excel.handler.export;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.excel.entity.ExcelExportInfo;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * byte[] 图片 处理器
 *
 * @author pengsenhao
 **/
public class ExcelByteImageExportHandler extends ExcelBufferedImageExportHandler {

    @Override
    public void exec(Field field, ExcelProperty excelProperty, Object obj, ExcelExportInfo info) throws IOException {
        byte[] bytes = (byte[]) obj;
        if (bytes == null || bytes.length == 0) {
            return;
        }
        BufferedImage bufferedImage;
        String suffix;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
             ImageInputStream stream = ImageIO.createImageInputStream(inputStream);
             ByteArrayInputStream imageInputStream = new ByteArrayInputStream(bytes)) {
            suffix = this.getSuffix(stream);
            bufferedImage = ImageIO.read(imageInputStream);
        }
        this.setBufferedImage(bufferedImage);
        this.setSuffix(suffix);
        super.exec(field, excelProperty, bytes, info);
    }

}
