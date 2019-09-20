package com.example.psh.controllers;

import com.example.psh.entities.Product;
import com.example.psh.services.ProductService;
import com.example.psh.utils.convertors.ProductConverter;
import org.example.soap.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class ProductControllerSOAP {

    private static final String NAMESPACE_URI = "http://example.org/soap";
    private static final Logger logger = LoggerFactory.getLogger(ProductControllerSOAP.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductConverter converter;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllProductsRequest")
    @ResponsePayload
    public GetAllProductsResponse getAllProducts(@RequestPayload GetAllProductsRequest request) {

        logger.info("SOAP adapter's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");

        GetAllProductsResponse response = new GetAllProductsResponse();
        List<Product> products = productService.getAllProducts();
        response.getProducts().addAll(converter.convertToProductInfoList(products));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetProductByIdRequest")
    @ResponsePayload
    public GetProductByIdResponse getProductById(@RequestPayload GetProductByIdRequest request) {

        logger.info("SOAP adapter's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");

        GetProductByIdResponse response = new GetProductByIdResponse();
        Product product = productService.getProductById(request.getId());
        response.setProduct(converter.convertToProductInfo(product));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddProductRequest")
    @ResponsePayload
    public AddProductResponse addProduct(@RequestPayload AddProductRequest request) {

        logger.info("SOAP adapter's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");

        AddProductResponse response = new AddProductResponse();
        Product receivedProduct =  converter.convertToProduct(request.getProduct());
        receivedProduct.setId(null);
        Product addedProduct = productService.addProduct(receivedProduct);
        response.setProduct(converter.convertToProductInfo(addedProduct));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SearchRequest")
    @ResponsePayload
    public SearchResponse searchProducts(@RequestPayload SearchRequest request) {

        logger.info("SOAP adapter's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");

        SearchResponse response = new SearchResponse();
        List<String> names = productService.searchProducts(request.getName(), request.getParameter(), request.getValue());
        response.getProductNames().addAll(names);
        return response;
    }
}
