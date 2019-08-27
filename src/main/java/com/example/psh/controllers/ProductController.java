package com.example.psh.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.psh.entities.Product;
import com.example.psh.services.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	ProductService service;
	
	@PostMapping(value = "/", produces = "application/JSON")
	public Product addProduct(@RequestBody Product product) {
		return service.addProduct(product);
	}
	
	@GetMapping(value = "/", produces = "application/JSON")
	public List<Product> getAllProducts() {
		return service.getAllProducts();
	}
	
	@GetMapping(value = "/{id}", produces = "application/JSON")
	public Product getProductById(@PathVariable String id) {
		return service.getProductById(id);
	}
	
	@GetMapping(value = "/search", produces = "application/JSON")
	public List<String> searchProducts(@RequestParam(required=false) String name,
			                           @RequestParam(required=false) String parameter, 
			                           @RequestParam(required=false) String value) {
		return service.searchProducts(name, parameter, value);
	}

}
