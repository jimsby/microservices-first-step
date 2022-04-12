package com.example.resourcep.async;

import com.example.resourcep.config.RabbitMockConfig;
import com.example.resourcep.controller.MetadataController;
import com.example.resourcep.dto.ResponseCustomIdsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Import(RabbitMockConfig.class)
class RabbitMqListenerTest {

    @MockBean
    MetadataController controller;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ObjectMapper mapper;

    @Test
    void listenCreate() throws JsonProcessingException, InterruptedException {
        ResponseCustomIdsDto dto = new ResponseCustomIdsDto(1);
        doNothing().when(controller).createMetadata(any());

        rabbitTemplate
                .convertAndSend("songs", "create",
                        mapper.writeValueAsString(dto));

        Thread.sleep(100);
        verify(controller).createMetadata(any());
    }
}