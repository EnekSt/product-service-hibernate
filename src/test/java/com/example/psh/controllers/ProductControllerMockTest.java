package com.example.psh.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.psh.entities.Parameter;
import com.example.psh.entities.Product;
import com.example.psh.services.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    public void testAddProduct() throws Exception {

        String mockedId = "mockedid";

        /*Product sentProduct = new Product(null, "prod test", "description of prod test",
                Arrays.asList(new Parameter("par1", "value1"), new Parameter("par2", "value2")));*/

        Product mockedProduct = new Product(mockedId, "prod test", "description of prod test",
                Arrays.asList(new Parameter("par1", "value1"), new Parameter("par2", "value2")));

        Mockito.when(service.addProduct(Mockito.any(Product.class))).thenReturn(mockedProduct);

        String content = "{\"name\":\"prod test\"," +
                "\"description\":\"description of prod test\"," +
                "\"parameters\":[{\"key\":\"par1\",\"value\":\"value1\"},{\"key\":\"par2\",\"value\":\"value2\"}]}";

        this.mockMvc.perform(post("/product/")
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockedId));
    }

    @Test
    public void testGetAllProducts() throws Exception {

        Product pr1 = new Product("anygeneratedid1", "prod test 1", "description of prod test 1",
                Arrays.asList(new Parameter("par1", "value1"), new Parameter("par2", "value2")));
        Product pr2 = new Product("anygeneratedid2", "prod test 2", "description of prod test 2",
                Arrays.asList(new Parameter("par1", "value1")));
        Product pr3 = new Product("anygeneratedid3", "prod test 3", null,
                Arrays.asList(new Parameter("par1", "value1"), new Parameter("par3", "value3")));
        List<Product> products = Arrays.asList(pr1, pr2, pr3);

        Mockito.when(service.getAllProducts()).thenReturn(products);

        this.mockMvc.perform(get("/product/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testSearchByName() throws Exception {

        List<String> foundNames = Arrays.asList("product N", "product N", "product N", "product N");

        Mockito.when(service.searchProducts(Mockito.anyString(), Mockito.isNull(), Mockito.isNull())).thenReturn(foundNames);

        this.mockMvc.perform(get("/product/search?name=product N"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product = new Product("anygeneratedid", "prod test", "description of prod test",
                Arrays.asList(new Parameter("par1", "value1"), new Parameter("par2", "value2"), new Parameter("par3", "value3")));

        Mockito.when(service.getProductById(Mockito.anyString())).thenReturn(product);

        this.mockMvc.perform(get("/product/anygeneratedid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("anygeneratedid"));
    }

}
