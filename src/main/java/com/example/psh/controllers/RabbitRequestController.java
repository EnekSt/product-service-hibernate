package com.example.psh.controllers;

import com.example.psh.config.RabbitConfig;
import com.example.psh.entities.Product;
import com.example.psh.services.ProductService;
import com.example.psh.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(RabbitRequestController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    // TODO: May be improve using another objects rather than String


    @RabbitListener(queues = RabbitConfig.GET_ALL_PRODUCTS_QUEUE, errorHandler = "errorHandlerForRabbit")
    @SendTo(RabbitConfig.QUEUE_RESPONSES)
    public String processGetAllProductsRequest(String meaninglessMessage) {

        logger.debug("RabbitRequestReceiver's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");
        logger.debug("Message received: {}", meaninglessMessage);

        List<Product> products = productService.getAllProducts();
        String result = JsonUtils.asJsonString(products);

        logger.trace("Result string: {}", result);
        return result;
    }

    @RabbitListener(queues = RabbitConfig.GET_PRODUCT_BY_ID_QUEUE, errorHandler = "errorHandlerForRabbit")
    @SendTo(RabbitConfig.QUEUE_RESPONSES)
    public String processGetProductByIdRequest(String id) {

        logger.debug("RabbitRequestReceiver's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");
        logger.debug("Message received: id = {}", id);

        /*Product product = null;
        try {
            product = productService.getProductById(id);
        } catch (RuntimeException ex) {
            logger.error("Error occurred in processGetProductByIdRequest method");
        }*/
        Product product = productService.getProductById(id);
        String result = JsonUtils.asJsonString(product);

        logger.trace("Result string: {}", result);
        return result;
    }

    @RabbitListener(queues = RabbitConfig.ADD_PRODUCT_QUEUE, errorHandler = "errorHandlerForRabbit")
    @SendTo(RabbitConfig.QUEUE_RESPONSES)
    public String processAddProductRequest(String jsonProductToAdd) {

        logger.debug("RabbitRequestReceiver's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");
        logger.debug("Message received: {}", jsonProductToAdd);


        Product product = null;
        try {
            product = objectMapper.readValue(jsonProductToAdd, Product.class);
            product.setId(null);
        } catch (IOException ex) {
            logger.error("IOException occurred in processAddProductRequest Method");
            throw new RuntimeException("IOException occurred in processAddProductRequest Method");
        }

        product = productService.addProduct(product);
        String result = JsonUtils.asJsonString(product);

        logger.trace("Result string: {}", result);
        return result;
    }

    @RabbitListener(queues = RabbitConfig.SEARCH_PRODUCTS_QUEUE, errorHandler = "errorHandlerForRabbit")
    @SendTo(RabbitConfig.QUEUE_RESPONSES)
    public String processSearchProductsRequest(String jsonParameterMap) {

        logger.debug("RabbitRequestReceiver's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");
        logger.debug("Message received: {}", jsonParameterMap);

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

        String result = JsonUtils.asJsonString(names);

        logger.trace("Result string: {}", result);
        return result;
    }
}
