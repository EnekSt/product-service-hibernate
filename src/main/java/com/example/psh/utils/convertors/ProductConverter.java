package com.example.psh.utils.convertors;

import com.example.psh.entities.Product;
import org.example.soap.ProductInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductConverter {

    @Autowired
    private ParameterConverter converter;

    // Product to ProductInfo

    public ProductInfo convertToProductInfo(Product product) {

        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(product, productInfo);

        productInfo.getParameters().addAll(converter.convertToParameterInfoList(product.getParameters()));
        return productInfo;
    }

    public List<ProductInfo> convertToProductInfoList(List<Product> products) {

        List<ProductInfo> productInfos = new ArrayList<>();
        for (Product product : products) {
            productInfos.add(convertToProductInfo(product));
        }
        return productInfos;
    }

    // ProductInfo to Product

    public Product convertToProduct(ProductInfo productInfo) {
        Product product = new Product();
        //BeanUtils.copyProperties(productInfo, product);
        product.setId(productInfo.getId());
        product.setName(productInfo.getName());
        product.setDescription(productInfo.getDescription());

        product.getParameters().addAll(converter.convertToParameterList(productInfo.getParameters()));
        return product;
    }

    public List<Product> convertToProductList(List<ProductInfo> productInfos) {
        List<Product> products = new ArrayList<>();
        for (ProductInfo productInfo : productInfos) {
            products.add(convertToProduct(productInfo));
        }
        return products;
    }




}
