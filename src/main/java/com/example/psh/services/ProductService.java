package com.example.psh.services;

import java.util.List;

import com.example.psh.entities.Product;

public interface ProductService { 
	
	Product getProductById(String id);
	
	List<Product> getAllProducts();
	
	Product addProduct(Product product);
	
	List<String> searchProducts(String name, String parameter, String value);

}
