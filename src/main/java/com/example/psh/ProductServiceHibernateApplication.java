package com.example.psh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ProductServiceHibernateApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceHibernateApplication.class, args);
	}

}
