package com.eq.shoppingcart.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductRestClient {

    private final RestTemplate restTemplate;
    public ProductRestClient(){
        restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> getProductInfo(String url) {
         return restTemplate.getForEntity(url, String.class);
    }

}
