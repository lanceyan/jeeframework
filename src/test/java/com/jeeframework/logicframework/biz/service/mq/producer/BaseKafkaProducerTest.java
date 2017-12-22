package com.jeeframework.logicframework.biz.service.mq.producer;

import com.jeeframework.testframework.AbstractSpringBaseTestNoTransaction;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述
 *
 * @author lance
 * @version 1.0 2017-12-21 18:08
 */
public class BaseKafkaProducerTest extends AbstractSpringBaseTestNoTransaction {

    @Autowired
    BaseKafkaProducer kafkaProducer;

    @Test
    public void send() throws Exception {
        kafkaProducer.send("test", "hahahaha ");
    }

}