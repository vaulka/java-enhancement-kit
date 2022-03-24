package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.Excel;
import com.pongsky.kit.excel.entity.ExcelExportInfo;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * URL 图片 处理器
 *
 * @author pengsenhao
 **/
public class ExcelUrlImageHandler extends ExcelBufferedImageHandler {

    @Override
    public void exec(Excel excel, Object obj, ExcelExportInfo info) throws IOException {
        String imageUrl = null;
        if (obj instanceof String) {
            imageUrl = obj.toString();
        }
        if (imageUrl == null) {
            return;
        }
        BufferedImage bufferedImage;
        String suffix;
        try (InputStream inputStream = new URL(imageUrl).openStream();
             ImageInputStream stream = ImageIO.createImageInputStream(inputStream)) {
            suffix = this.getSuffix(stream);
            bufferedImage = ImageIO.read(new URL(imageUrl));
        }
        this.setBufferedImage(bufferedImage);
        this.setSuffix(suffix);
        super.exec(excel, obj, info);
    }

}
