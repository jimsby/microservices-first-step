package com.example.resourcep.async;

import com.example.resourcep.dto.ResponseCustomIdsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RabbitMqListener {

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "songs-created")
    public void listenCreate (String data){
        try {
            ResponseCustomIdsDto dto = objectMapper.readValue(data, ResponseCustomIdsDto.class);
            log.info("created objects: " + data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "songs-deleted")
    public void listenDelete (String data){
        try {
            ResponseCustomIdsDto dto = objectMapper.readValue(data, ResponseCustomIdsDto.class);
            log.info("deleted objects: " + data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
