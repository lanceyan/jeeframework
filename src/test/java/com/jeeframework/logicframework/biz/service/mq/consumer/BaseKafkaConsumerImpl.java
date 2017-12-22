/*
 * @project: com.jeeframework 
 * @package: com.jeeframework.logicframework.biz.service.mq.consumer
 * @title:   BaseKafkaConsumerImpl.java 
 *
 * Copyright (c) 2017 jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.biz.service.mq.consumer;

/**
 * 描述
 *
 * @author lance
 * @version 1.0 2017-12-21 19:42
 */
public class BaseKafkaConsumerImpl extends BaseKafkaConsumer {


    @Override
    public void dealMessage(String message) {
        System.out.println("收到消息    >>>>>>>>>>   " + message);
    }
}
