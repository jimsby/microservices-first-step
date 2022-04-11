package com.ms.resource.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    private static final String QUEUE_NAME_1 = "songs-created";
    private static final String QUEUE_NAME_2 = "songs-deleted";
    private static final String ROUTING_KEY_1 = "create";
    private static final String ROUTING_KEY_2 = "delete";
    private static final String EXCHANGE_NAME = "songs";

    @Bean
    public Queue queue1(){
        return new Queue(QUEUE_NAME_1, false);
    }

    @Bean
    public Queue queue2(){
        return new Queue(QUEUE_NAME_2, false);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding1(@Qualifier("queue1") Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_1);
    }

    @Bean
    public Binding binding2(@Qualifier("queue2") Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_2);
    }
}
