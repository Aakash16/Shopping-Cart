package com.eq.shoppingcart.service;

import com.eq.shoppingcart.client.ProductRestClient;
import com.eq.shoppingcart.constants.ProductConstant;
import com.eq.shoppingcart.exception.InvalidProductException;
import com.eq.shoppingcart.exception.ProductNotFoundException;
import com.eq.shoppingcart.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    @Value("${product.info.url.cheerios}")
    private String cheeriosInfoUrl;

    @Value("${product.info.url.cornflakes}")
    private String cornflakesInfoUrl;

    @Value("${product.info.url.frosties}")
    private String frostiesInfoUrl;

    @Value("${product.info.url.shreddies}")
    private String shreddiesInfoUrl;

    @Value("${product.info.url.weetabix}")
    private String weetabixInfoUrl;

    @Autowired
    private ProductRestClient productRestClient;

    public List<Product> getProductList(List<String> productNameList) {
      return productNameList.stream().map(productName -> getProduct(productName.trim())).collect(Collectors.toList());
    }

    private Product getProduct(String productName)  {

        if(ObjectUtils.isEmpty(productName)){
            log.error("Product name invalid: {}", productName);
            throw new InvalidProductException("Product name not valid");
        }

        String url = getUrl(productName);
        log.info("Fetching product with URL: {}", url);

        ResponseEntity<String> response = productRestClient.getProductInfo(url);
        log.debug("Fetched productInfo for:{}, with info:{}", productName, response.getBody());

        try {
            return new ObjectMapper().readValue(response.getBody(), Product.class);
        } catch (JsonProcessingException e) {
            log.error("Error reading product information from client response");
            throw new ProductNotFoundException("Error fetching product info");
        }
    }

    private String getUrl(String productName) {
        try {
            ProductConstant.Products products = ProductConstant.Products.valueOf(productName.toUpperCase());
            return switch (products) {
                case CHEERIOS -> cheeriosInfoUrl;
                case CORNFLAKES -> cornflakesInfoUrl;
                case FROSTIES -> frostiesInfoUrl;
                case SHREDDIES -> shreddiesInfoUrl;
                case WEETABIX -> weetabixInfoUrl;
            };
        } catch (IllegalArgumentException ex) {
            log.error("Product doesn't exists in inventory.");
            throw new ProductNotFoundException("Product doesn't exists in inventory");
        }
    }
}
