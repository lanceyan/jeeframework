/**
 * @project: with
 * @Title: ImageUtil.java
 * @Package: com.webdemo.util.image
 * <p/>
 * Copyright (c) 2014-2014 Transing Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.webdemo.util.image;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * TODO 简单描述
 * <p>
 *
 * @Description: TODO 详细描述
 * @author TODO
 * @version 1.0 2015-3-18 下午03:29:54
 */
public class ImageUtil {

    public static String resize(int width, int height, String srcImage) throws ImageFormatException, IOException {

        String ext = com.jeeframework.util.io.FileUtils.getExtention(srcImage);
        int _pos = srcImage.lastIndexOf("_");
        String destImagePrefix = srcImage.substring(0, _pos);

        BufferedImage targetImage = ImageIO.read(new File(srcImage));

        String destImage = destImagePrefix + "_" + width + ext;

        width = Math.max(width, 1);
        height = Math.max(height, 1);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(targetImage, 0, 0, width, height, null);

        FileOutputStream fos = new FileOutputStream(new File(destImage));
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
        encoder.encode(image);
        image.flush();
        fos.flush();
        fos.close();

        return destImage;
    }

    public static void main(String[] args) throws IOException {

        String destPath = resize(260, 260, "F:\\work\\个人事情\\为知\\运营\\发布假数据\\quokka_640.jpg");

        System.out.println(destPath);
    }

}
