package com.example.psh.controllers;

import com.example.psh.entities.Parameter;
import com.example.psh.entities.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductControllerIntegrationTest {

    private final String baseURI = "http://localhost:8080/product";

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    @Test
    public void testGetProductById() throws JSONException {

        // Id of existing product in database
        final String givenId = "5d775761a7b11b0001317666";
        final String path = "/" + givenId;

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Product> response = restTemplate.exchange(
                baseURI + path,
                HttpMethod.GET, entity, Product.class);

        ObjectMapper mapper = new ObjectMapper();

        /*String expected = "{\"id\":\"5d775761a7b11b0001317666\"," +
                "\"name\":\"product 1\"," +
                "\"description\":\"Description of product 1\"," +
                "\"parameters\":[{\"key\":\"par1\",\"value\":\"par1_val\"},{\"key\":\"par2\",\"value\":\"par2_val\"}]}";*/

        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getId(), givenId);
        Assert.assertNotNull(response.getBody().getName());
    }

    @Test
    public void testCreateNewProduct() {

        Product product = new Product(null, "test product", "product created by a test method",
                Arrays.asList(new Parameter("param1", "param1val")));

        final String path = "/";
        HttpEntity<Product> entity = new HttpEntity<>(product, headers);

        ResponseEntity<Product> response = restTemplate.exchange(
                baseURI + path,
                HttpMethod.POST, entity, Product.class);

        Product actual = response.getBody();
        Assert.assertNotNull(actual);
        Assert.assertNotNull(actual.getId());

        //TODO: somehow remove inserted test product
    }

    @Test
    public void testGetProductsWithParameter() {

        final String path = "/search?parameter=par1";

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List> response = restTemplate.exchange(
                baseURI + path,
                HttpMethod.GET, entity, List.class);
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getBody().size() >= 1);
    }

    @Test
    public void testGetProductsByValueOfParameter() {

        final String path = "/search?parameter=par1&par1_val";

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List> response = restTemplate.exchange(
                baseURI + path,
                HttpMethod.GET, entity, List.class);
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getBody().size() >= 1);
    }

    @Test
    public void testGetProductsByName() {

        final String path = "/search?name=test product";

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List> response = restTemplate.exchange(
                baseURI + path,
                HttpMethod.GET, entity, List.class);
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getBody().stream().allMatch(a -> a.equals("test product")));
    }

}
