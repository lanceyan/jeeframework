/**
 * @project: with 
 * @Title: ChatUtil.java 
 * @Package: com.webdemo.util
 *
 * Copyright (c) 2014-2014 Transing Limited, Inc.
 * All rights reserved.
 * 
 */
package com.jeeframework.webdemo.util;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.util.validate.Validate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO 简单描述
 * <p>
 * 
 * @Description: TODO 详细描述
 * @author TODO
 * @version 1.0 2015-3-18 上午10:53:07
 */
public class ChatUtil {
    /**
     * 简单描述：生成聊天会话的唯一id
     * <p>
     * 
     * @param uidInt
     * @param chatUidInt
     * @return
     */
    public static String makeChatSessionId(long uidInt, long chatUidInt) {
        List<Long> uidList = new ArrayList<Long>();
        uidList.add(uidInt);
        uidList.add(chatUidInt);
        Collections.sort(uidList);

        String fromToId = StringUtils.join(uidList, "_");
        return fromToId;
    }

    /**
     * 简单描述：上传聊天的背景图，语音
     * <p>
     * 
     * @param bgPic
     * @param filePrefix
     * @param req
     * @param partyId
     * @throws IOException
     */
    public static String createPartyChatFile(MultipartFile bgPic, String filePrefix, HttpServletRequest req, String partyId) throws BizException {

        String endPath = "";
        try {
            if (bgPic != null && !bgPic.isEmpty()) {
                String rootPath = req.getSession().getServletContext().getRealPath("/") + File.separator + "static";
                // rootPath=FileUtil.getParent(rootPath)+ File.separator+"webapps";
                endPath = File.separator + "party" + File.separator + "chat" + File.separator + partyId + File.separator;
                rootPath += endPath;
                File rootPathFile = new File(rootPath);
                if (!rootPathFile.exists()) {
                    rootPathFile.mkdirs();
                }
                String suffix = filePrefix + RandomUtils.nextInt(100) + com.jeeframework.util.io.FileUtils.getExtention(bgPic.getOriginalFilename());
                endPath = endPath + suffix;

                //拿到输出流，同时重命名上传的文件  
                //拿到上传文件的输入流  

                //                rootPath += File.separator;
                File target = new File(rootPath, suffix);
                bgPic.transferTo(target);

            }
        } catch (IOException e) {
            throw new BizException("上传聊天文件失败");
        }
        if (endPath.indexOf("\\") >= 0) {
            endPath = endPath.replaceAll("\\\\", "/");
        }
        return endPath;
    }

    /**
     * 简单描述：上传聊天的背景图，语音
     * <p>
     * 
     * @param bgPic
     * @param filePrefix
     * @param req
     * @param fromToId
     * @throws IOException
     */
    public static String createChatFile(MultipartFile bgPic, String filePrefix, HttpServletRequest req, String fromToId) throws BizException {

        String endPath = "";
        try {
            if (bgPic != null && !bgPic.isEmpty()) {
                String rootPath = req.getSession().getServletContext().getRealPath("/") + File.separator + "static";
                // rootPath=FileUtil.getParent(rootPath)+ File.separator+"webapps";
                endPath = File.separator + "chat" + File.separator + fromToId + File.separator;
                rootPath += endPath;
                File rootPathFile = new File(rootPath);
                if (!rootPathFile.exists()) {
                    rootPathFile.mkdirs();
                }
                String suffix = filePrefix + RandomUtils.nextInt(100) + com.jeeframework.util.io.FileUtils.getExtention(bgPic.getOriginalFilename());
                endPath = endPath + suffix;

                //拿到输出流，同时重命名上传的文件  
                //拿到上传文件的输入流  

                //                rootPath += File.separator;
                File target = new File(rootPath, suffix);
                bgPic.transferTo(target);

            }
        } catch (IOException e) {
            throw new BizException("上传聊天文件失败");
        }
        if (endPath.indexOf("\\") >= 0) {
            endPath = endPath.replaceAll("\\\\", "/");
        }
        return endPath;
    }

    /**
     * 简单描述：组装静态资源的绝对URL
     * <p>
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
        String host =  WebUtil.getStaticServerByEnv();

//        return "http://" + host + "/static" + staticURI;
        return "http://" + host +   staticURI;

    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(RandomUtils.nextInt(100));
        }
    }
}
