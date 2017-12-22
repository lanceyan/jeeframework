package com.jeeframework.logicframework.biz.service.mq.consumer;

import com.jeeframework.testframework.AbstractSpringBaseTestNoTransaction;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述
 *
 * @author lance
 * @version 1.0 2017-12-21 19:40
 */
public class BaseKafkaConsumerTest extends AbstractSpringBaseTestNoTransaction {
    @Autowired
    BaseKafkaConsumer kafkaConsumer;

    @Test
    public void dealMessage() throws Exception {
        while (true) {
            Thread.sleep(1000);
        }

    }

}