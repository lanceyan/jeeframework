/**
 * @project: with
 * @Title: WebUtil.java
 * @Package: com.webdemo.util
 * <p/>
 * Copyright (c) 2014-2014 Transing Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.webdemo.util;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.util.io.FileUtils;
import com.jeeframework.util.validate.Validate;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Web端数据工具类
 * <p/>
 *
 * @author TODO
 * @version 1.0 2015-3-4 下午02:34:50
 * @Description: TODO 详细描述
 */
public class WebUtil {
    /**
     * <b>function:</b> 将Image的宽度、高度缩放到指定width、height，并保存在savePath目录
     *
     * @param width    缩放的宽度
     * @param height   缩放的高度
     * @param srcImage
     * @param req
     * @return 图片保存路径、名称
     * @throws ImageFormatException
     * @throws IOException
     * @author lance
     * @createDate 2015-2-6 下午04:54:35
     */
    public static String resize(int width, int height, String srcImage, HttpServletRequest req) throws ImageFormatException, IOException {

        String destImage = srcImage;
        String ext = com.jeeframework.util.io.FileUtils.getExtention(srcImage);
        int _pos = destImage.lastIndexOf("_");
        String destImagePrefix = destImage.substring(0, _pos);

        String rootPath = req.getSession().getServletContext().getRealPath("/") + File.separator + "static";
        String srcPath = rootPath + srcImage;
        BufferedImage targetImage = ImageIO.read(new File(srcPath));

        String suffix = destImagePrefix + "_" + width + ext;
        destImage = rootPath + suffix;

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

        return suffix;
    }

    /**
     * 根据质量压缩图片
     *
     * @param quality
     * @param srcImage
     * @param req
     * @return
     * @throws ImageFormatException
     * @throws IOException
     */
    public static String resize(float quality, String srcImage, HttpServletRequest req) throws ImageFormatException, IOException {

        String destImage = srcImage;
        String ext = com.jeeframework.util.io.FileUtils.getExtention(srcImage);
        int _pos = destImage.lastIndexOf(".");
        String destImagePrefix = destImage.substring(0, _pos);

        String rootPath = req.getSession().getServletContext().getRealPath("/") + File.separator + "static";
        String srcPath = rootPath + srcImage;
        BufferedImage srcImageBuffer = ImageIO.read(new File(srcPath));

        int width = srcImageBuffer.getWidth();
        int height = srcImageBuffer.getHeight();

        String suffix = destImagePrefix + "_" + quality + ext;
        destImage = rootPath + suffix;

        width = Math.max(width, 1);
        height = Math.max(height, 1);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(srcImageBuffer, 0, 0, width, height, null);

        FileOutputStream fos = new FileOutputStream(new File(destImage));
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);

        JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(srcImageBuffer);
        /** 压缩质量 */
        jep.setQuality(quality, true);

        encoder.encode(image, jep);
        image.flush();
        fos.flush();
        fos.close();

        return suffix;
    }

    /**
     * 简单描述：根据文件流保存原图
     * <p/>
     *
     * @param avatarBytes
     * @param req
     * @param userId
     * @throws IOException
     */
    public static String saveOriginalAvatar(byte[] avatarBytes, HttpServletRequest req, String userId) throws BizException {

        String endPath = "";
        try {
            if (avatarBytes != null && avatarBytes.length > 0) {
                String rootPath = req.getSession().getServletContext().getRealPath("/") + File.separator + "static";

                endPath = File.separator + "avatar" + File.separator + userId + File.separator;
                rootPath += endPath;

                String suffix = userId + "_0.jpg";

                endPath = endPath + suffix;

                //拿到输出流，同时重命名上传的文件  
                //拿到上传文件的输入流  
                File rootPathFile = new File(rootPath);
                if (!rootPathFile.exists()) {
                    rootPathFile.mkdirs();
                } else {
                    FileUtils.deleteDirectory(rootPathFile);
                    rootPathFile.mkdirs();
                }

                File target = new File(rootPath, suffix);

                //                avatar.transferTo(target);
                FileUtils.writeByteArrayToFile(target, avatarBytes);

            }
        } catch (IOException e) {
            throw new BizException("保存原头像失败" + e);
        }

        //        if (endPath.indexOf("\\") >= 0) {
        //            endPath = endPath.replaceAll("\\\\", "/");
        //        }
        return endPath;
    }

    /**
     * 简单描述：根据文件流、手机号、请求创建图片文件夹及路径
     * <p/>
     *
     * @param avatar
     * @param req
     * @param userId
     * @throws IOException
     */
    public static String createAvatar(MultipartFile avatar, HttpServletRequest req, String userId) throws BizException {

        String endPath = "";
        try {
            // rootPath=FileUtil.getParent(rootPath)+ File.separator+"webapps";

            if (!avatar.isEmpty()) {
                String rootPath = req.getSession().getServletContext().getRealPath("/") + File.separator + "static";

                endPath = File.separator + "avatar" + File.separator + userId + File.separator;
                rootPath += endPath;

                String suffix = userId + com.jeeframework.util.io.FileUtils.getExtention(avatar.getOriginalFilename());

                endPath = endPath + suffix;

                //拿到输出流，同时重命名上传的文件  
                //拿到上传文件的输入流  
                File rootPathFile = new File(rootPath);
                if (!rootPathFile.exists()) {
                    rootPathFile.mkdirs();
                } else {
                    FileUtils.deleteDirectory(rootPathFile);
                    rootPathFile.mkdirs();
                }

                File target = new File(rootPath, suffix);

                avatar.transferTo(target);

            }
        } catch (IOException e) {
            throw new BizException("上传头像失败" + e);
        }

//        if (endPath.indexOf("\\") >= 0) {
//            endPath = endPath.replaceAll("\\\\", "/");
//        }
        return endPath;
    }

    /**
     * 简单描述：根据文件流、频道ID、请求创建频道封面文件夹及路径
     * <p/>
     *
     * @param partyCover
     * @param req
     * @param partyId
     * @throws IOException
     */
    public static String createPartyCover(MultipartFile partyCover, HttpServletRequest req, long partyId) throws BizException {

        String endPath = "";
        try {
            if (!partyCover.isEmpty()) {
                String rootPath = req.getSession().getServletContext().getRealPath("/") + File.separator + "static";
                // rootPath=FileUtil.getParent(rootPath)+ File.separator+"webapps";
                endPath = File.separator + "party" + File.separator + partyId + File.separator;
                rootPath += endPath;
                String suffix = partyId + "_cover" + com.jeeframework.util.io.FileUtils.getExtention(partyCover.getOriginalFilename());

                endPath = endPath + suffix;

                //拿到输出流，同时重命名上传的文件  
                //拿到上传文件的输入流  
                File rootPathFile = new File(rootPath);
                if (!rootPathFile.exists()) {
                    rootPathFile.mkdirs();
                }

                rootPath += File.separator;
                File target = new File(rootPath, suffix);
                partyCover.transferTo(target);

            }
        } catch (IOException e) {
            throw new BizException("上传频道封面失败");
        }
        if (endPath.indexOf("\\") >= 0) {
            endPath = endPath.replaceAll("\\\\", "/");
        }
        return endPath;
    }

    public static String createPartyCover(byte[] partyCover, HttpServletRequest req, long partyId) throws BizException {

        String endPath = "";
        try {
            if (partyCover != null && partyCover.length > 0) {
                String rootPath = req.getSession().getServletContext().getRealPath("/") + File.separator + "static";
                // rootPath=FileUtil.getParent(rootPath)+ File.separator+"webapps";
                endPath = File.separator + "party" + File.separator + partyId + File.separator;
                rootPath += endPath;
                String suffix = partyId + "_cover.jpg";

                endPath = endPath + suffix;

                //拿到输出流，同时重命名上传的文件
                //拿到上传文件的输入流
                File rootPathFile = new File(rootPath);
                if (!rootPathFile.exists()) {
                    rootPathFile.mkdirs();
                }

                rootPath += File.separator;
                File target = new File(rootPath, suffix);

                FileUtils.writeByteArrayToFile(target, partyCover);

            }
        } catch (IOException e) {
            throw new BizException("上传频道封面失败");
        }
        if (endPath.indexOf("\\") >= 0) {
            endPath = endPath.replaceAll("\\\\", "/");
        }
        return endPath;
    }

    public static String createPartyPhoto(byte[] partyCover, HttpServletRequest req, long uid) throws BizException {

        String endPath = "";
        try {
            if (partyCover != null && partyCover.length > 0) {
                String rootPath = req.getSession().getServletContext().getRealPath("/") + File.separator + "static";
                endPath = File.separator + "user" + File.separator + uid + File.separator + "photos" + File.separator;
                rootPath += endPath;
                String suffix = System.currentTimeMillis() + RandomUtils.nextInt(100) + ".jpg";
                endPath = endPath + suffix;
                //拿到输出流，同时重命名上传的文件
                //拿到上传文件的输入流
                File rootPathFile = new File(rootPath);
                if (!rootPathFile.exists()) {
                    rootPathFile.mkdirs();
                }

                rootPath += File.separator;
                File target = new File(rootPath, suffix);

                FileUtils.writeByteArrayToFile(target, partyCover);

            }
        } catch (IOException e) {
            throw new BizException("上传频道照片失败");
        }
        if (endPath.indexOf("\\") >= 0) {
            endPath = endPath.replaceAll("\\\\", "/");
        }
        return endPath;
    }

    public static String createPartyFile(MultipartFile partyPhoto, HttpServletRequest req, long uid) throws BizException {

        String endPath = "";
        try {
            if (!partyPhoto.isEmpty()) {
                String rootPath = req.getSession().getServletContext().getRealPath("/") + File.separator + "static";
                // rootPath=FileUtil.getParent(rootPath)+ File.separator+"webapps";
                endPath = File.separator + "user" + File.separator + uid + File.separator + "file" + File.separator;
                rootPath += endPath;
                String suffix = System.currentTimeMillis() + RandomUtils.nextInt(100) + com.jeeframework.util.io.FileUtils.getExtention(partyPhoto.getOriginalFilename());

                endPath = endPath + suffix;
                //拿到输出流，同时重命名上传的文件  
                //拿到上传文件的输入流  
                File rootPathFile = new File(rootPath);
                if (!rootPathFile.exists()) {
                    rootPathFile.mkdirs();
                }

                File target = new File(rootPath, suffix);

                partyPhoto.transferTo(target);

            }
        } catch (IOException e) {
            throw new BizException("上传频道照片失败");
        }
        if (!Validate.isEmpty(endPath)) {
            if (endPath.indexOf("\\") >= 0) {
                endPath = endPath.replaceAll("\\\\", "/");
            }
        }
        return endPath;
    }

    /**
     * 简单描述：根据文件流、频道ID、请求创建频道封面文件夹及路径
     * <p/>
     *
     * @param partyPhotos
     * @param req
     * @param partyId
     * @throws IOException
     */
    public static List<String> createPartyPhotos(MultipartFile[] partyPhotos, HttpServletRequest req, int partyId) throws BizException {

        List<String> partyPhotoPaths = new ArrayList<String>();
        if (partyPhotos != null && partyPhotos.length > 0) {
            for (int i = 0; i < partyPhotos.length; i++) {
                MultipartFile partyPhoto = partyPhotos[i];
                String endPath = "";
                try {
                    if (!partyPhoto.isEmpty()) {
                        String rootPath = req.getSession().getServletContext().getRealPath("/") + File.separator + "static";
                        // rootPath=FileUtil.getParent(rootPath)+ File.separator+"webapps";
                        endPath = File.separator + "party" + File.separator + partyId + File.separator + "photos" + File.separator;
                        rootPath += endPath;
                        String suffix = i + com.jeeframework.util.io.FileUtils.getExtention(partyPhoto.getOriginalFilename());

                        endPath = endPath + suffix;
                        //拿到输出流，同时重命名上传的文件  
                        //拿到上传文件的输入流  
                        File rootPathFile = new File(rootPath);
                        if (!rootPathFile.exists()) {
                            rootPathFile.mkdirs();
                        }

                        File target = new File(rootPath, suffix);

                        partyPhoto.transferTo(target);

                    }
                } catch (IOException e) {
                    throw new BizException("上传频道照片失败");
                }
                if (!Validate.isEmpty(endPath)) {
                    if (endPath.indexOf("\\") >= 0) {
                        endPath = endPath.replaceAll("\\\\", "/");
                    }
                    partyPhotoPaths.add(endPath);
                }
            }
        }
        return partyPhotoPaths;
    }

    /**
     * 简单描述：组装静态资源的绝对URL
     * <p/>
     *
     * @param staticURI
     * @return
     */
    public static String combinateStaticURL(String staticURI) {

        if (Validate.isEmpty(staticURI)) {
            return "";
        }
        if (staticURI.startsWith("http://")) {
            return staticURI;
        }

        String host = getStaticServerByEnv();

//        return "http://" + host + "/static" + staticURI;
        return "http://" + host + staticURI;

    }

    /**
     * 简单描述：组装频道短链接URL
     * <p/>
     *
     * @param staticURI
     * @return
     */
    public static String combinatePartyShortURL(String staticURI) {

        if (Validate.isEmpty(staticURI)) {
            return "";
        }

        String host = getWebServerByEnv();

        return "http://" + host + "/p/" + staticURI;

    }

    /**
     * 简单描述：返回用户的头像
     * <p/>
     *
     * @param avatarURI
     * @param width     0 原图 640 260 100 4种规格
     * @return
     */
    public static String getUserAvatar(String avatarURI, int width) {

        if (Validate.isEmpty(avatarURI)) {
            return ""; //返回系统默认头像
        }
        if (avatarURI.startsWith("http://")) {
            return avatarURI;
        }

        String ext = com.jeeframework.util.io.FileUtils.getExtention(avatarURI);
        int _pos = avatarURI.lastIndexOf("_");
        String destImagePrefix = "";
        if (_pos >= 0) {
            destImagePrefix = avatarURI.substring(0, _pos);
        }
        String suffix = destImagePrefix + "_" + width + ext;

        String host = getStaticServerByEnv();

//        return "http://" + host + "/static" + suffix;
        return "http://" + host + suffix;

    }

    /**
     * 简单描述：根据默认封面的id返回相对URL
     * <p/>
     *
     * @param coverId
     * @return
     */
    public static String getSystemDefaultCoverURL(String coverId) {

        if (Validate.isEmpty(coverId)) {
            return "";
        }


        return "/party/system/sys_cover_" + coverId + ".jpg";

    }


    /**
     * 返回web服务器的地址
     *
     * @return
     */
    public static String getWebServerByEnv() {
        String host = "www.weicitech.com";

        String confEnv = System.getProperty("conf.env");
        if (!Validate.isEmpty(confEnv)) {
            if (confEnv.equals("local")) {
                host = "192.168.1.121";
//                host = "www.weicitech.com";
            }
            if (confEnv.equals("dev")) {
                host = "192.168.1.123";
            }
            if (confEnv.equals("gamma")) {
                host = "gamma.weicitech.com";
            }
            if (confEnv.equals("idc")) {
                host = "www.weicitech.com";
            }
        }
        return host;
    }

    /**
     * 返回App的服务器地址
     *
     * @return
     */
    public static String getAppServerByEnv() {
        String host = "www.weicitech.com";

        String confEnv = System.getProperty("conf.env");
        if (!Validate.isEmpty(confEnv)) {
            if (confEnv.equals("local")) {
                host = "192.168.1.121";
//                host = "www.weicitech.com";
            }
            if (confEnv.equals("dev")) {
                host = "192.168.1.123";
            }
            if (confEnv.equals("gamma")) {
                host = "gamma.weicitech.com";
            }
            if (confEnv.equals("idc")) {
                host = "www.weicitech.com";
            }
        }
        return host;
    }

    /**
     * 返回静态资源服务器的地址
     *
     * @return
     */
    public static String getStaticServerByEnv() {
        String host = "static.weicitech.com";

        String confEnv = System.getProperty("conf.env");
        if (!Validate.isEmpty(confEnv)) {
            if (confEnv.equals("local")) {
                host = "192.168.1.121";
//                host = "www.weicitech.com";
            }
            if (confEnv.equals("dev")) {
                host = "192.168.1.123";
            }
            if (confEnv.equals("gamma")) {
                host = "staticgamma.weicitech.com";
            }
            if (confEnv.equals("idc")) {
                host = "static.weicitech.com";
            }
        }
        return host;
    }

    public static String getWeiXinAPIIDByEnv() {

        String APP_ID = "wxe7f9fe59f1884492";

        String confEnv = System.getProperty("conf.env");
        if (!Validate.isEmpty(confEnv)) {
            if (confEnv.equals("local")) {
                APP_ID = "wx86e4c430c0a88946";
            }
            if (confEnv.equals("dev")) {
                APP_ID = "wx86e4c430c0a88946";
            }
            if (confEnv.equals("gamma")) {
                APP_ID = "wx86e4c430c0a88946";
            }
            if (confEnv.equals("idc")) {
                APP_ID = "wxe7f9fe59f1884492";
            }
        }
        return APP_ID;
    }

    public static String getWeiXinAPISecretByEnv() {
        String APP_SECRET = "5fbb46343e1cf40acf9135d7f8f1cd0d";

        String confEnv = System.getProperty("conf.env");
        if (!Validate.isEmpty(confEnv)) {
            if (confEnv.equals("local")) {
                APP_SECRET = "1e5902c88beb0393995522d1710d6871";
            }
            if (confEnv.equals("dev")) {
                APP_SECRET = "1e5902c88beb0393995522d1710d6871";
            }
            if (confEnv.equals("gamma")) {
                APP_SECRET = "1e5902c88beb0393995522d1710d6871";
            }
            if (confEnv.equals("idc")) {
                APP_SECRET = "5fbb46343e1cf40acf9135d7f8f1cd0d";
            }
        }
        return APP_SECRET;
    }

}
