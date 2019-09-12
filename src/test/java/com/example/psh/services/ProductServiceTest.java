package com.example.psh.services;

import com.example.psh.entities.Parameter;
import com.example.psh.entities.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class ProductServiceTest {

    @MockBean
    private ProductService service;

    @Test
    public void addProductTest() {

        String mockedId = "mockedid";
        Product mockedProduct = new Product(mockedId, "prod test", "description of prod test",
                Arrays.asList(new Parameter("par1", "value1"), new Parameter("par2", "value2")));

        Mockito.when(service.addProduct(Mockito.any(Product.class))).thenReturn(mockedProduct);

        Product actual = service.addProduct(new Product());

        Assert.assertNotNull(actual);
        Assert.assertEquals(mockedProduct, actual);
    }

    @Test
    public void testGetAllProducts() {

        Product pr1 = new Product("anygeneratedid1", "prod test 1", "description of prod test 1",
                Arrays.asList(new Parameter("par1", "value1"), new Parameter("par2", "value2")));
        Product pr2 = new Product("anygeneratedid2", "prod test 2", "description of prod test 2",
                Arrays.asList(new Parameter("par1", "value1")));
        Product pr3 = new Product("anygeneratedid3", "prod test 3", null,
                Arrays.asList(new Parameter("par1", "value1"), new Parameter("par3", "value3")));
        List<Product> mockedList = Arrays.asList(pr1, pr2, pr3);

        Mockito.when(service.getAllProducts()).thenReturn(mockedList);

        List<Product> actualList = service.getAllProducts();

        Assert.assertNotNull(actualList);
        Assert.assertEquals(mockedList, actualList);
    }

    @Test
    public void testSearchByName() {

        List<String> foundNames = Arrays.asList("product N", "product N", "product N", "product N");

        Mockito.when(service.searchProducts(Mockito.anyString(), Mockito.isNull(), Mockito.isNull())).thenReturn(foundNames);

        List<String> actualList = service.searchProducts("product N", null, null);

        Assert.assertNotNull(actualList);
        Assert.assertEquals(4, actualList.size());
        Assert.assertTrue(actualList.stream().allMatch(name -> name.equals("product N")));
    }

    @Test
    public void testGetProductById() {

        String mockedId = "anygeneratedid";
        Product mockedProduct = new Product(mockedId, "prod test", "description of prod test",
                Arrays.asList(new Parameter("par1", "value1"), new Parameter("par2", "value2"), new Parameter("par3", "value3")));

        Mockito.when(service.getProductById(Mockito.anyString())).thenReturn(mockedProduct);

        Product actual = service.getProductById(mockedId);

        Assert.assertNotNull(actual);
        Assert.assertEquals(mockedId, actual.getId());
    }
}
