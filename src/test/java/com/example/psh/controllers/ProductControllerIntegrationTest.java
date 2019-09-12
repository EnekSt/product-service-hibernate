package com.example.psh.controllers;

import com.example.psh.entities.Parameter;
import com.example.psh.entities.Product;
import com.example.psh.feignclients.ProductClient;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductControllerIntegrationTest {

    @Autowired
    private ProductClient productClient;

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("product-service-hibernate");
    private EntityManager em = emf.createEntityManager();

    private List<Product> testProducts = new ArrayList<>();

    @Before
    public void init() {

        // Create Test Data
        Product pr1 = new Product(null, "journal", "description of journal",
                Arrays.asList(new Parameter("par1", "val1"), new Parameter("par2", "val2")));
        Product pr2 = new Product(null, "mat", "description of mat",
                Arrays.asList(new Parameter("par1", "newval1"), new Parameter("par3", "val3")));
        Product pr3 = new Product(null, "mousepad", "description of mousepad",
                Arrays.asList(new Parameter("par2", "val2"), new Parameter("par3", "newval3")));
        testProducts.add(pr1);
        testProducts.add(pr2);
        testProducts.add(pr3);

        em.getTransaction().begin();
        em.persist(pr1);
        em.persist(pr2);
        em.persist(pr3);
        em.flush();
        em.getTransaction().commit();
    }

    @Test
    public void getAllProductsTest() {
        List<Product> products = productClient.getAllProducts();
        Assert.assertNotNull(products);
        Assert.assertTrue(products.size() >= 3);
    }

    @Test
    public void addProductTest() {

        Product product = new Product(null, "New Product", "New description of new product",
                Arrays.asList(new Parameter("par1", "val1"), new Parameter("newpar", "newval")));

        Product result = productClient.addProduct(product);
        testProducts.add(result);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());
        Assert.assertEquals(product.getName(), result.getName());
        Assert.assertEquals(product.getDescription(), result.getDescription());
    }

    @Test
    public void searchProductsByNameTest() {
        List<String> names = productClient.searchProducts("mousepad", null, null);
        Assert.assertNotNull(names);
        Assert.assertFalse(names.isEmpty());
        Assert.assertTrue(names.stream().allMatch(name -> name.equals("mousepad")));
    }

    @Test
    public void searchProductsWithParameter() {
        List<String> names = productClient.searchProducts(null, "par1", null);
        Assert.assertNotNull(names);
        Assert.assertFalse(names.isEmpty());
    }

    @Test
    public void searchProductByParametersValueTest() {
        List<String> names = productClient.searchProducts(null, "par2", "val2");
        Assert.assertNotNull(names);
        Assert.assertTrue(names.size() >= 2);
    }

    @Test
    public void getProductByIdTest() {
        String existingId = testProducts.get(1).getId();
        Product product = productClient.getProductById(existingId);
        Assert.assertNotNull(product);
        Assert.assertEquals(existingId, product.getId());
    }

    @After
    public void shutdown() {
        em.getTransaction().begin();
        for (Product pr : testProducts) {
            em.remove(em.contains(pr) ? pr : em.merge(pr));
        }
        em.flush();
        em.getTransaction().commit();
    }
}
