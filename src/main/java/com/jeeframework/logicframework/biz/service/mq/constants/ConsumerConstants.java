/*
 * @project: hyfaycrawler
 * @package: com.hyfaycrawler.util.mq.constants
 * @title:   ConsumerConstants.java
 *
 * Copyright (c) 2014 Hyfay Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.biz.service.mq.constants;

/**
 * 消息消费者常量
 *
 * @author lance
 * @version 1.0 2014/7/7 11:10
 */
public final class ConsumerConstants {
    private ConsumerConstants() {/**/}

    /**
     * 默认的参数
     */
    public static final String[][] DEFAULT_CONFIG = new String[][]{
            {"zk.sessiontimeout.ms", "400"},
            {"zk.connectiontimeout.ms", "6000"},
            {"zk.synctime.ms", "200"},
            {"socket.timeout.ms", "30000"},
            {"socket.buffersize", String.valueOf(64 * 1024)},
            {"fetch.size", String.valueOf(300 * 1024)},
            {"queuedchunks.max", "100"},
            {"autocommit.enable", "true"},
            {"autocommit.interval.ms", "10000"},
            {"autooffset.reset", "smallest"},
            {"rebalance.retries.max", "10"},
            {"groupid", "consumerGroup"},
            {"zookeeper.session.timeout.ms", "400"},
            {"zookeeper.sync.time.ms", "200"},
            {"auto.commit.interval.ms", "1000"}
    };
    /**
     * 固定的参数
     */
    public static final String[][] FIXED_CONFIG = new String[][]{
            {"consumer.timeout.ms", "-1"}
    };
}

