package com.example.psh.config;

import com.example.psh.errorhandling.MyErrorHandlerForRabbit;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String GET_ALL_PRODUCTS_QUEUE = "getAllProductsQueue";
    public static final String GET_PRODUCT_BY_ID_QUEUE = "getProductByIdQueue";
    public static final String ADD_PRODUCT_QUEUE = "addProductQueue";
    public static final String SEARCH_PRODUCTS_QUEUE = "searchProductsQueue";

    public static final String QUEUE_RESPONSES = "responses-queue";

    @Bean
    Queue getAllProductsQueue() {
        return new Queue(GET_ALL_PRODUCTS_QUEUE);
    }

    @Bean
    Queue getProductByIdQueue() {
        return new Queue(GET_PRODUCT_BY_ID_QUEUE);
    }

    @Bean
    Queue addProductQueue() {
        return new Queue(ADD_PRODUCT_QUEUE);
    }

    @Bean
    Queue searchProductsQueue() {
        return new Queue(SEARCH_PRODUCTS_QUEUE);
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
