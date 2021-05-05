package com.example.sjs.controller;

import com.example.sjs.dto.MessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Value("${demo.rocketmq.topic}")
    private String topicName;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @PostMapping(path = "/message")
    public void createMessage(@RequestBody MessageDTO message) {
        log.info("Create Message {}", message);

        try {
            this.rocketMQTemplate.send(this.topicName,
                    MessageBuilder.withPayload(
                            new ObjectMapper().writeValueAsString(message)
                    ).build());
        } catch (JsonProcessingException e) {
            log.error("Error", e);
        }
    }
}
