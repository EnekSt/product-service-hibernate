package com.example.psh.config;

import com.example.psh.errorhandling.MyErrorHandlerForRabbit;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_REQUESTS = "requests-queue";

    public static final String QUEUE_RESPONSES = "responses-queue";

    @Bean
    Queue requestsQueue() {
        return new Queue(QUEUE_REQUESTS);
    }

    // Do we need it?
    @Bean
    Queue responsesQueue() {
        return new Queue(QUEUE_RESPONSES);
    }

    @Bean(name = "errorHandlerForRabbit")
    RabbitListenerErrorHandler errorHandler() {
        return new MyErrorHandlerForRabbit();
    }
}
