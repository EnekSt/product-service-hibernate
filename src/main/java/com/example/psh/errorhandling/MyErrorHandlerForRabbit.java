package com.example.psh.errorhandling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;

public class MyErrorHandlerForRabbit implements RabbitListenerErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyErrorHandlerForRabbit.class);
    @Override
    public Object handleError(Message message, org.springframework.messaging.Message<?> message1, ListenerExecutionFailedException e) throws Exception {
        logger.info("Some Exception Occurred in RabbitListener");
        return "Some Exception Occurred in RabbitListener and was handle in MyErrorHandlerForRabbit";
    }
}
