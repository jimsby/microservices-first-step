package com.ms.resource.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.resource.dto.ResponseCustomIdsDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;


@Controller
@AllArgsConstructor
public class RabbitController {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void sendCreated(ResponseCustomIdsDto send) {

        try {
            rabbitTemplate.convertAndSend("songs", "create", objectMapper.writeValueAsString(send));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendDeleted(ResponseCustomIdsDto send) {
        try {
            rabbitTemplate.convertAndSend("songs", "delete", objectMapper.writeValueAsString(send));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
