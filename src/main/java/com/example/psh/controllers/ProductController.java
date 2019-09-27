package com.example.psh.controllers;

import java.util.List;

import com.example.psh.errorhandling.exceptions.InvalidIdRepresentationException;
import com.example.psh.errorhandling.exceptions.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.persistence.NoResultException;
import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

	private final static Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductService service;
	
	@PostMapping(value = "", produces = "application/JSON")
	public Product addProduct(@Valid @RequestBody Product product) {
		logger.debug("Controller's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");
		return service.addProduct(product);
	}
	
	@GetMapping(value = "", produces = "application/JSON")
	public List<Product> getAllProducts() {
        logger.debug("Controller's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");
		return service.getAllProducts();
	}
	
	@GetMapping(value = "/{id}", produces = "application/JSON")
	public Product getProductById(@PathVariable String id) {
        logger.debug("Controller's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");

		try {
			return service.getProductById(id);
		}
		catch (NoResultException ex) {
			throw new ProductNotFoundException(id);
		}
		catch (IllegalArgumentException ex) {
			throw new InvalidIdRepresentationException(id);
		}
	}
	
	@GetMapping(value = "/search", produces = "application/JSON")
	public List<String> searchProducts(@RequestParam(required=false) String name,
			                           @RequestParam(required=false) String parameter, 
			                           @RequestParam(required=false) String value) {
        logger.debug("Controller's " + (new Object() {}.getClass().getEnclosingMethod().getName()) + " method was called");
		return service.searchProducts(name, parameter, value);
	}
}
