/*
 * @project: hyfaycrawler 
 * @package: com.hyfaycrawler.util.mq.producer
 * @title:   Producer.java 
 *
 * Copyright (c) 2014 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.biz.service.mq.consumer;

/**
 * 消息消费者接口，必须实现处理方法的方法
 *
 * @author lance
 * @version 1.0 2014/7/4 17:59
 */
public interface BaseConsumer {
    /**
     * 消费者接收消息的处理方法实现
     * @param message
     */
    public <K, V> void dealMessage(String message);

}
