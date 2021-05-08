package com.example.sjs.service;

import com.example.sjs.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@ExtRocketMQTemplateConfiguration(group = "tran-group")
public class OrderRocketMQTemplate extends RocketMQTemplate {

    @Value("${demo.rocketmq.topic}")
    private String topicName;

    public void send(Order order) {
        try {
            this.sendMessageInTransaction(this.topicName,
                    MessageBuilder
                            .withPayload(new ObjectMapper().writeValueAsString(order))
                            .setHeader(RocketMQHeaders.TRANSACTION_ID, UUID.randomUUID())
                            .build(),
                    null);
        } catch (JsonProcessingException e) {
            log.error("Error", e);
        }
    }
}
