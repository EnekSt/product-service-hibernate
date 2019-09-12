package com.example.psh.feignclients;

import com.example.psh.entities.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "products", url = "http://localhost:8080")
public interface ProductClient {

    @RequestMapping(method = RequestMethod.GET, value = "/product/")
    List<Product> getAllProducts();

    @RequestMapping(method = RequestMethod.POST, value = "/product/")
    Product addProduct(@RequestBody Product product);

    @RequestMapping(method = RequestMethod.GET, value = "/product/{id}")
    Product getProductById(@PathVariable("id") String id);

    @RequestMapping(method = RequestMethod.GET, value = "/product/search")
    List<String> searchProducts(@RequestParam(required=false) String name,
                                @RequestParam(required=false) String parameter,
                                @RequestParam(required=false) String value);
}
