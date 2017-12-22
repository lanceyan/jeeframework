/*
 * @project: hyfaycrawler 
 * @package: com.hyfaycrawler.util.mq.producer
 * @title:   Producer.java 
 *
 * Copyright (c) 2014 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.biz.service.mq.producer;

/**
 * 消息生产者
 *
 * @author lance
 * @version 1.0 2014/7/4 17:59
 */
public interface BaseProducer {
    /**
     * 向消息系统中send数据 一个主题的随机分区，发送一条个数据。
     *
     * @param topic
     * @param data
     */
    <K, V> void send(String topic, V data);

//    /**
//     * 向消息系统中send数据 一个主题的随机分区，发送多条个数据。
//     *
//     * @param topic
//     * @param datas
//     */
//    public void send(String topic, List<V> datas);
//
//    /**
//     * 向消息系统中send数据 一个主题的指定的一个分区，发送多条个数据。
//     *
//     * @param topic
//     * @param key
//     * @param datas
//     */
//    public void send(String topic, K key, List<V> datas);

}
