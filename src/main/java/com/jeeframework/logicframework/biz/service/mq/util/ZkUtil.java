/*
 * @project: hyfaycrawler 
 * @package: com.hyfaycrawler.util.mq.util
 * @title:   ZkUtil.java 
 *
 * Copyright (c) 2014 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.biz.service.mq.util;

import com.jeeframework.util.validate.Validate;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * zookeeper的工具类
 *
 * @author lance
 * @version 1.0 2014/7/7 11:07
 */
public final class ZkUtil {

    private static final Logger logger = Logger.getLogger(ZkUtil.class);

    private static final String defaultZkAddress = "localhost:2181";

    //开发环境zk地址
    private static final String defaultRootPath = "/";


    /**
     * 根据zookeeper具体路径，生成kafka的zk.connect
     *
     * @param path
     * @return
     */
    private static String genZkConnectStr(String kafkaZookeeperConnect, String path) {

        String zkAdress = kafkaZookeeperConnect;


        if (Validate.isEmpty(zkAdress)) {
            zkAdress = defaultZkAddress;
            logger.debug("zookeeper.connect 没有设置，采用默认配置 " + defaultZkAddress);
//            logger.debugTrace(ZkUtil.class.getSimpleName(), "zookeeper.ip 没有设置，采用默认配置 " + defaultZkIp);
        }


        String zkHost = zkAdress + "%s";


        if (StringUtils.isBlank(path)) {
            return String.format(zkHost, defaultRootPath);
        } else {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            return String.format(zkHost, path);
        }
    }

    public static String getZkRootPath(String kafkaZookeeperConnect, String
            kafkaZookeeperNamespace) {

        return genZkConnectStr(kafkaZookeeperConnect, kafkaZookeeperNamespace);
    }

}
