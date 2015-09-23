package com.jeeframework.util.image;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

public class ImageUtils {
	
	 /**
     * ����һ������������ͼƬ��ʽ<code>String</code>  
     * 
     * @author lanceyan
     * @since v1.0
     * 
     * <p> Ĭ�Ϸ���һ���մ�</p>
     *
     * @param byte[] ����������
     * 
     * @exception IOException if an error occurs reading the
     * information from the input source.
     *
     * @return ͼƬ��ʽ, as a <code>String</code>.
     */
	public static String getImageType(byte[] imageContent){
		String type = "";
		ByteArrayInputStream byteInput = null;
		MemoryCacheImageInputStream memoryInput = null;
		byteInput = new ByteArrayInputStream(imageContent);
		memoryInput = new MemoryCacheImageInputStream(byteInput);
		Iterator<?> itr = ImageIO.getImageReaders(memoryInput);
		while (itr.hasNext()) {
			ImageReader reader = (ImageReader) itr.next();
			try {
				type = reader.getFormatName();
			} catch (IOException e) {			
				//e.printStackTrace();
			}
		}

		return type;
	}
	
	/**
	 * 
     * ����һ������������ͼƬ��ʽ�Ƿ���ȷ<code>boolean</code>  
     * 
     * @author lanceyan
     * @since v1.0
     * 
	 * @param imageContent������������
	 * @param imageType��ͼƬ��ʽ
     *
	 * @throws IOException if an error occurs reading the
     * information from the input source.
     * 
     * @return boolean
	 */
	public static boolean checkImageType(byte[] imageContent, String imageType){
		return imageType.equalsIgnoreCase(getImageType(imageContent));
	}
}
