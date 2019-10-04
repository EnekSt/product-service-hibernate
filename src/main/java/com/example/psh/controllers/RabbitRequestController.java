package com.example.psh.controllers;

import com.example.psh.config.RabbitConfig;
import com.example.psh.entities.Product;
import com.example.psh.services.ProductService;
import com.example.psh.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RabbitRequestController {

    private static final String ERROR_HANDLER = "errorHandlerForRabbit";
    private static final String SERVICE_METHOD_HEADER = "service-method";

    private static final Logger logger = LoggerFactory.getLogger(RabbitRequestController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;


    @RabbitListener(queues = RabbitConfig.QUEUE_REQUESTS, errorHandler = ERROR_HANDLER)
    @SendTo(RabbitConfig.QUEUE_RESPONSES)
    public String processRabbitRequest(Message message) {

        String header = (String)message.getMessageProperties().getHeaders().get(SERVICE_METHOD_HEADER);
        String stringMessage = new String(message.getBody());
        logger.debug("Message received: {}", stringMessage);

        String result = "";

        switch (header) {
            case "getAllProducts":
                result = processGetAllProductsRequest(stringMessage);
                break;
            case "getProductById":
                result = processGetProductByIdRequest(stringMessage);
                break;
            case "addProduct":
                result = processAddProductRequest(stringMessage);
                break;
            case "searchProducts":
                result = processSearchProductsRequest(stringMessage);
                break;
            default:
                logger.debug("No handler found for given header: {}", header);
        }

        logger.trace("Result string: {}", result);
        return result;
    }

    private String processGetAllProductsRequest(String meaninglessMessage) {

        logger.debug("RabbitRequestReceiver's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");

        List<Product> products = productService.getAllProducts();
        return JsonUtils.asJsonString(products);
    }

    private String processGetProductByIdRequest(String id) {

        logger.debug("RabbitRequestReceiver's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");

        /*Product product = null;
        try {
            product = productService.getProductById(id);
        } catch (RuntimeException ex) {
            logger.error("Error occurred in processGetProductByIdRequest method");
        }*/
        Product product = productService.getProductById(id);
        return JsonUtils.asJsonString(product);
    }

    private String processAddProductRequest(String jsonProductToAdd) {

        logger.debug("RabbitRequestReceiver's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");

        Product product = null;
        try {
            product = objectMapper.readValue(jsonProductToAdd, Product.class);
            product.setId(null);
        } catch (IOException ex) {
            logger.error("IOException occurred in processAddProductRequest Method");
            throw new RuntimeException("IOException occurred in processAddProductRequest Method");
        }

        product = productService.addProduct(product);
        return JsonUtils.asJsonString(product);
    }

    private String processSearchProductsRequest(String jsonParameterMap) {

        logger.debug("RabbitRequestReceiver's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");

        Map<String, String> queryParameters = new HashMap<>();
        try {
            queryParameters = objectMapper.readValue(jsonParameterMap, new TypeReference<Map<String, String>>(){});
        } catch (IOException ex) {
            logger.error("IOException occurred in processSearchProductsRequest Method");
            throw new RuntimeException("IOException occurred in processSearchProductsRequest Method");
        }

        List<String> names = productService.searchProducts(
                queryParameters.get("name"),
                queryParameters.get("parameter"),
                queryParameters.get("value"));

        return JsonUtils.asJsonString(names);
    }
}
