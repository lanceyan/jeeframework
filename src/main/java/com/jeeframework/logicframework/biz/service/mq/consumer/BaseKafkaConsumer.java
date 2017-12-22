/*
 * @project: hyfaycrawler 
 * @package: com.hyfaycrawler.util.mq.producer
 * @title:   KafkaConsumer.java 
 *
 * Copyright (c) 2014 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.biz.service.mq.consumer;


import com.jeeframework.logicframework.beans.ContextManageBean;
import com.jeeframework.logicframework.biz.exception.BizException;
import com.jeeframework.logicframework.biz.service.mq.util.KafkaUtil;
import com.jeeframework.util.validate.Validate;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * kafka消费者
 *
 * @author lance
 * @version 1.0 2014/7/7 11:10
 */
public abstract class BaseKafkaConsumer implements BaseConsumer, ContextManageBean {


    private static final Logger logger = Logger.getLogger(BaseKafkaConsumer.class);
    private static final int KAFKA_SERVER_CPUS = 4;

    private int threadNum = 0; //线程数量
    private String groupId = null;  //消息消费者的分组id
    private String topic = null; //监听的topic名字

    private String beanName = null;//当前生产者的名字
    protected BeanFactory context;

    protected String kafkaZookeeperConnect = null;//kafka zookeeper 连接url
    protected String kafkaZookeeperNamespace = null;//kafka zookeeper namespace
    protected String kafkaNamespace = null;


    /**
     * 测试结果 10万条数据耗时10秒，每秒1万条左右。
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        if (Validate.isEmpty(topic)) {
            throw new BizException("topic 没有设置，必须要配置！");
        }

        if (!Validate.isEmpty(this.kafkaNamespace)) {
            topic = this.kafkaNamespace + "_" + topic;
        }

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1));

        Map props = new Properties();

        if (!Validate.isEmpty(groupId)) {
            props.put("group.id", groupId);
        }


        ConsumerConfig consumerConfig = KafkaUtil.getDefaultConsumerConfig(kafkaZookeeperConnect,
                kafkaZookeeperNamespace, props);


        ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        final KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);

        if (threadNum == 0) {
            threadNum = KAFKA_SERVER_CPUS;
        }

        ExecutorService executor = Executors.newFixedThreadPool(threadNum);

        executor.submit(new Runnable() {
            public void run() {
                ConsumerIterator<byte[], byte[]> it = stream.iterator();
                while (it.hasNext()) {
//                        System.out.println("get one message " + msgAndMetadata.message());
                    dealMessage(new String(it.next().message()));

                }
            }
        });

        System.out.println("按照当前消费者配置" + consumerConfig.props() + "\r\n  consumer《" + this.beanName + "》-topic《" +
                topic + "》启动了  " + threadNum + "个线程。");
    }


    /**
     * 消费者接收消息的处理方法实现
     *
     * @param message
     */
    public abstract void dealMessage(String message);

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    @Override
    public void destroy() throws Exception {

    }

    public void setKafkaZookeeperConnect(String kafkaZookeeperConnect) {
        this.kafkaZookeeperConnect = kafkaZookeeperConnect;
    }

    public void setKafkaZookeeperNamespace(String kafkaZookeeperNamespace) {
        this.kafkaZookeeperNamespace = kafkaZookeeperNamespace;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.context = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = beanName;
    }

    public void setKafkaNamespace(String kafkaNamespace) {
        this.kafkaNamespace = kafkaNamespace;
    }
}

