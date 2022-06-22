package com.matrix.um.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author yihaosun
 * @date 2022/6/22 13:55
 */
@Component
@Slf4j
public class KafkaUtils {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 发送kafka消息
     *
     * @param topicName
     * @param jsonMessage
     */
    public void send(String topicName, String jsonMessage) {
        kafkaTemplate.send(topicName, jsonMessage);
    }
}
