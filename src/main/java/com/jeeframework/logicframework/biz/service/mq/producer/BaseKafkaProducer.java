/*
 * @project: hyfaycrawler 
 * @package: com.hyfaycrawler.util.mq.producer.impl
 * @title:   KafkaProducerImpl.java 
 *
 * Copyright (c) 2014 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.biz.service.mq.producer;

import com.jeeframework.logicframework.beans.ContextManageBean;
import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.mq.util.KafkaUtil;
import com.jeeframework.util.validate.Validate;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * kafka生产消息实现者
 *
 * @author lance
 * @version 1.0 2014/7/4 18:01
 */
public class BaseKafkaProducer implements BaseProducer, ContextManageBean {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(BaseKafkaProducer.class);

    private kafka.javaapi.producer.Producer producer = null; //当前生产者

    private String beanName = null;//当前生产者的名字
    protected BeanFactory context;

    private String brokerList;//消息中间件的地址列表  ip:端口  逗号分隔

    protected String kafkaNamespace = null;
//    private String kafkaZookeeperConnect = null;//kafka zookeeper 连接url
//    private String kafkaZookeeperNamespace = null;//kafka zookeeper namespace

    /**
     * 向kafka中send数据
     *
     * @param topic
     * @param key   The default partitioning strategy is hash(key)%numPartitions. If the key is null, then a random
     *              broker partition is picked。
     * @param datas
     */
    public <K, V> void send(String topic, K key, List<V> datas) {
        if (!Validate.isEmpty(this.kafkaNamespace)) {
            topic = this.kafkaNamespace + "_" + topic;
        }
        List<KeyedMessage<K, V>> keyMessageList = new ArrayList<KeyedMessage<K, V>>();

        if (!Validate.isEmpty(datas)) {
            for (V data : datas) {
                keyMessageList.add(new KeyedMessage<K, V>(topic, data));
            }
        }
        if (producer == null) {
            throw new BizException("生产者没有加载，可能是当前配置的角色指定错误!");
        }

//            logger.error(String.format("kafka send error.topic:[%s],key:[%s],data:[%s]", topic, key, datas), e);
        producer.send(keyMessageList);

    }

    /**
     * 向kafka中send数据
     *
     * @param topic
     * @param datas
     */
    public <K, V> void send(String topic, List<V> datas) {
        if (!Validate.isEmpty(this.kafkaNamespace)) {
            topic = this.kafkaNamespace + "_" + topic;
        }
        List<KeyedMessage<K, V>> keyMessageList = new ArrayList<KeyedMessage<K, V>>();

        if (!Validate.isEmpty(datas)) {
            for (V data : datas) {
                keyMessageList.add(new KeyedMessage<K, V>(topic, data));
            }
        }
        if (producer == null) {
            throw new BizException("生产者没有加载，可能是当前配置的角色指定错误!");
        }
        producer.send(keyMessageList);
//            logger.error(String.format("kafka send error.topic:[%s],data:[%s]", topic, datas), e);
    }

    /**
     * 向kafka中send数据
     *
     * @param topic
     * @param data
     */
    @Override
    public <K, V> void send(String topic, V data) {
        if (producer == null) {
            throw new BizException("生产者没有加载，可能是当前配置的角色指定错误!");
        }
        if (!Validate.isEmpty(this.kafkaNamespace)) {
            topic = this.kafkaNamespace + "_" + topic;
        }
        producer.send(new KeyedMessage<K, V>(topic, data));
//            logger.error(String.format("kafka send error.topic:[%s],data:[%s]", topic, data), e);
    }

    @Override
    public void afterPropertiesSet() throws Exception {


        ProducerConfig producerConfig = KafkaUtil.getDefaultProducerConfig(brokerList);

        producer = new kafka.javaapi.producer.Producer(producerConfig);

        System.out.println("按照配置当前生产者 " + producerConfig.props() + "\r\n  produer《" + this.beanName + "》启动了 ");

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.context = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = beanName;
    }


    @Override
    public void destroy() throws Exception {

    }


    public void setKafkaNamespace(String kafkaNamespace) {
        this.kafkaNamespace = kafkaNamespace;
    }


    public void setBrokerList(String brokerList) {
        this.brokerList = brokerList;
    }


}