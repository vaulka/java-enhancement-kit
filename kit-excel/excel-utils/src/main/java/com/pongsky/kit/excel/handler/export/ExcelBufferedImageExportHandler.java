package com.pongsky.kit.excel.handler.export;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * 图片 处理器
 *
 * @author pengsenhao
 **/
@Setter
public abstract class ExcelBufferedImageExportHandler implements ExcelExportHandler {

    private BufferedImage bufferedImage = null;
    private String suffix = null;

    @Override
    public void exec(Field field, ExcelProperty excelProperty, Object obj, ExcelExportInfo info) throws IOException {
        if (bufferedImage == null) {
            return;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, suffix, outputStream);
        ClientAnchor anchor = new XSSFClientAnchor(10, 10, 10, 10,
                (short) info.getCell().getColumnIndex(), info.getCell().getRow().getRowNum(),
                (short) (info.getCell().getColumnIndex() + 1), info.getCell().getRow().getRowNum() + 1);
        try {
            info.getDrawing().createPicture(anchor, info.getWorkbook().addPicture(outputStream.toByteArray(), this.getPictureType(suffix)));
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
        info.setImageWidth(info.getCell().getColumnIndex(), bufferedImage.getWidth());
        info.setImageHeight(info.getCell().getRow().getRowNum(), bufferedImage.getHeight());
    }

    /**
     * 获取图片格式后缀
     *
     * @param inputStream inputStream
     * @return 获取图片格式后缀
     * @throws IOException IOException
     */
    public String getSuffix(ImageInputStream inputStream) throws IOException {
        Iterator<ImageReader> iterator = ImageIO.getImageReaders(inputStream);
        if (iterator.hasNext()) {
            ImageReader reader = iterator.next();
            return reader.getFormatName();
        }
        return "";
    }

    /**
     * 获取图片格式
     *
     * @param suffix 图片后缀
     * @return 获取图片格式
     */
    private int getPictureType(String suffix) {
        switch (suffix) {
            case "png":
                return HSSFWorkbook.PICTURE_TYPE_PNG;
            case "jpg":
            case "jpeg":
            default:
                return HSSFWorkbook.PICTURE_TYPE_JPEG;
        }
    }

}
