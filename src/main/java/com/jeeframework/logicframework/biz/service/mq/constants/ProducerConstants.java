/*
 * @project: hyfaycrawler
 * @package: com.hyfaycrawler.util.mq.constants
 * @title:   ProducerConstants.java
 *
 * Copyright (c) 2014 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.biz.service.mq.constants;


/**
 * 消息生产者常量
 *
 * @author lance
 * @version 1.0 2014/7/7 11:10
 */
public final class ProducerConstants {

    private ProducerConstants() {/**/}

    /**
     * 默认的参数
     */
    public static final String[][] DEFAULT_CONFIG = new String[][]{
            {"serializer.class", "kafka.serializer.StringEncoder"},
            // zookeeper的配置
            {"zk.sessiontimeout.ms", "400"},
            {"zk.connectiontimeout.ms", "6000"},
            {"zk.synctime.ms", "200"},
            // 默认配置
            {"buffer.size", "102400"},
            {"connect.timeout.ms", "5000"},
            {"socket.timeout.ms", "30000"},
            {"reconnect.interval", "30000"},
            {"max.message.size", "1000000"},
            {"compression.codec", "1"},
            /**
             * The producer using the zookeeper software load balancer maintains a ZK cache that gets
             * updated by the zookeeper watcher listeners. During some events like a broker bounce, the
             * producer ZK cache can get into an inconsistent state, for a small time period. In this time
             * period, it could end up picking a broker partition that is unavailable. When this happens, the
             * ZK cache needs to be updated.
             * This parameter specifies the number of times the producer attempts to refresh this ZK cache.
             *
             * see(@kafka.producer.
             *   BaseProducer private def zkSend(producerData: ProducerData[K, V]*): Unit)
             *</pre>
             */
            {"zk.read.num.retries", "10"},
            /**
             * If DefaultEventHandler is used, this specifies the number of times to
             * retry if an error is encountered during send. Currently, it is only
             * appropriate when broker.list points to a VIP. If the zk.connect option
             * is used instead, this will not have any effect because with the zk-based
             * producer, brokers are not re-selected upon retry. So retries would go to
             * the same (potentially still down) broker. (KAFKA-253 will help address
             * this.)
             *
             * see(@kafka.producer.async.DefaultEventHandler
             *   private def send(messagesPerTopic: Map[(String, Int), ByteBufferMessageSet], syncProducer: SyncProducer))
             */
            {"num.retries", "10"},
            //Options for Asynchronous Producers (producer.type=async)
            {"queue.time", "5000"},
            {"queue.size", "10000"},
            {"batch.size", "200"},
            /**
             * Timeout for event enqueue:
             * 0: events will be enqueued immediately or dropped if the queue is full
             * -ve: enqueue will block indefinitely if the queue is full
             * +ve: enqueue will block up to this many milliseconds if the queue is full
             */
            {"queue.enqueueTimeout.ms", "-1"}
    };

}
