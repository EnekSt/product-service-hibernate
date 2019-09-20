package com.example.psh.repositories;

import com.example.psh.entities.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("product-service-hibernate");
    private EntityManager em = emf.createEntityManager();

    @Override
    public Product getProductById(String id) {
        Query query = em.createQuery("SELECT p FROM Product p WHERE p.id = :id");
        query.setParameter("id", id);
        return (Product)query.getSingleResult();
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = em.createQuery("SELECT p FROM Product p").getResultList();
        return products;
    }

    @Override
    public Product addProduct(Product product) {
        em.getTransaction().begin();
        em.persist(product);
        em.flush();
        em.getTransaction().commit();
        return product;
    }

    @Override
    public List<String> searchProducts(String name, String parameter, String value) {

        StringBuilder query = new StringBuilder("db.products.find({");

        if (name != null) {
            query.append("'name': '").append(name).append("'");
            if (parameter != null) {
                query.append(", ");
            }
        }
        if (parameter != null) {
            query.append("'parameters': { '$elemMatch': { 'key': '").append(parameter).append("'");
            if (value != null) {
                query.append(", 'value': '").append(value).append("'");
            }
            query.append("} }");
        }
        query.append("})");

        List<Product> products = em.createNativeQuery( query.toString(), Product.class ).getResultList();

        List<String> listOfNames = new ArrayList<>();
        products.forEach((product) -> listOfNames.add(product.getName()));
        return listOfNames;
    }
}
