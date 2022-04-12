package com.ms.resource.config;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;

public class RabbitMockConfig {
    @Bean
    ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(new MockConnectionFactory());
    }
}
