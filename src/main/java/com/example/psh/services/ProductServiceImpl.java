package com.example.psh.services;

import java.util.List;

import com.example.psh.entities.Product;
import com.example.psh.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository repository;

	@Override
	public Product getProductById(String id) {
		return repository.getProductById(id);
	}

	@Override
	public List<Product> getAllProducts() {		
		return repository.getAllProducts();
	}

	@Override
	public Product addProduct(Product product) {		
		return repository.addProduct(product);
	}

	@Override
	public List<String> searchProducts(String name, String parameter, String value) {
		return repository.searchProducts(name, parameter, value);
	}
}
