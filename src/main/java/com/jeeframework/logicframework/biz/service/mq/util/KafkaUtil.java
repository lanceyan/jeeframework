/*
 * @project: hyfaycrawler 
 * @package: com.hyfaycrawler.util.mq.util
 * @title:   KafkaUtil.java 
 *
 * Copyright (c) 2014 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.biz.service.mq.util;

import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.mq.constants.ConsumerConstants;
import com.jeeframework.logicframework.biz.service.mq.constants.ProducerConstants;
import com.jeeframework.util.validate.Validate;
import kafka.consumer.ConsumerConfig;
import kafka.producer.ProducerConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * kafka消息中间件的工具类
 *
 * @author lance
 * @version 1.0 2014/7/7 11:09
 */
public final class KafkaUtil {
    private KafkaUtil() {/**/}

    /**
     * 构建kafka的消费者配置
     *
     * @param configMap
     * @return
     */

    public static ConsumerConfig createConsumerConfig(Map<String, String> configMap) {
        Properties props = new Properties();
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            props.put(entry.getKey(), entry.getValue());
        }
        return new ConsumerConfig(props);
    }

    /**
     * 构建kafka的生产者配置
     *
     * @param configMap
     * @return
     */

    public static ProducerConfig createProducerConfig(Map<String, String> configMap) {
        Properties props = new Properties();
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            props.put(entry.getKey(), entry.getValue());
        }
        return new ProducerConfig(props);
    }

    public static ProducerConfig getDefaultProducerConfig(String brokerList) {
        Map<String, String> configMap = new HashMap<String, String>();
        for (String[] entry : ProducerConstants.DEFAULT_CONFIG) {
            configMap.put(entry[0], entry[1]);
        }
//        configMap.put("zookeeper.connect", ZkUtil.getZkRootPath());

        if (Validate.isEmpty(brokerList)) {
            throw new BizException("mq.broker.list 没有设置，格式为“ip:port” ");
        }
//        configMap.put("zookeeper.connect", ZkUtil.getZkRootPath(kafkaZookeeperConnect, kafkaZookeeperNamespace));
        configMap.put("metadata.broker.list", brokerList);
        return createProducerConfig(configMap);
    }


    public static ConsumerConfig getDefaultConsumerConfig(String kafkaZookeeperConnect, String
            kafkaZookeeperNamespace, Map props) {
        Map<String, String> configMap = new HashMap<String, String>();
        for (String[] entry : ConsumerConstants.DEFAULT_CONFIG) {
            configMap.put(entry[0], entry[1]);
        }
        configMap.put("zookeeper.connect", ZkUtil.getZkRootPath(kafkaZookeeperConnect, kafkaZookeeperNamespace));
        if (!Validate.isEmpty(props)) {
            configMap.putAll(props);
        }
        return createConsumerConfig(configMap);
    }
}