package com.ms.resource.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.resource.dto.ResponseCustomIdsDto;
import com.ms.resource.model.AudioFile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class RabbitControllerTest {
    private static final ResponseCustomIdsDto send = new ResponseCustomIdsDto(1);

    @Mock
    ObjectMapper mapper;

    @Mock
    RabbitTemplate rabbitTemplate;

    @InjectMocks
    RabbitController rabbitController;

    @BeforeEach
    void setMockOutput() throws JsonProcessingException {
        String jsonAnswer = "{ \"id\": 1}";
        lenient().when(mapper.writeValueAsString(ArgumentMatchers.any(ResponseCustomIdsDto.class)))
                .thenReturn(jsonAnswer);
    }
    
    @Test
    void sendCreated() {
        rabbitController.sendCreated(send);

        verify(rabbitTemplate).convertAndSend(eq("songs"), eq("create"), anyString());
    }

    @Test
    void sendDeleted() {
        rabbitController.sendDeleted(send);

        verify(rabbitTemplate).convertAndSend(eq("songs"), eq("delete"), anyString());
    }
}