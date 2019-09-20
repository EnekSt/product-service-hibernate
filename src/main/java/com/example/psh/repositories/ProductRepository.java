package com.example.psh.repositories;

import com.example.psh.entities.Product;

import java.util.List;

public interface ProductRepository {

    Product getProductById(String id);

    List<Product> getAllProducts();

    Product addProduct(Product product);

    List<String> searchProducts(String name, String parameter, String value);
}
