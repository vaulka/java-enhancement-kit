package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.Excel;
import com.pongsky.kit.excel.entity.ExcelExportInfo;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * File 图片 处理器
 *
 * @author pengsenhao
 **/
public class ExcelFileImageHandler extends ExcelBufferedImageHandler {

    @Override
    public void exec(Excel excel, Object obj, ExcelExportInfo<?> info) throws IOException {
        File imageFile = null;
        if (obj instanceof File) {
            imageFile = (File) obj;
        }
        if (imageFile == null) {
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
        super.exec(excel, obj, info);
    }

}
