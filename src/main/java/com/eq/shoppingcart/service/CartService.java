package com.eq.shoppingcart.service;

import com.eq.shoppingcart.model.Cart;
import com.eq.shoppingcart.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class CartService {

    private final Cart cart;
    private final HashMap<String, Product> productHashMap;

    public CartService() {
        cart = new Cart(new ArrayList<>());
        productHashMap = new HashMap<>();
    }

    public List<Product> addToCart(List<Product> productList) {

        productList.stream().filter(this::isProductValid).forEach(this::saveToMap);
        cart.setProductList(productHashMap.values().stream().toList());
        log.info("Product list saved successfully: {}", productList.size());
        return productList;
    }

    public Cart getCart() {
        return cart;
    }

    private boolean isProductValid(Product product) {
        boolean hasTitle = !ObjectUtils.isEmpty(product.getTitle());
        boolean hasPrice = !ObjectUtils.isEmpty(product.getPrice());
        if (!hasTitle) {
            log.warn("Product doesn't contains title");
        }
        if (!hasPrice) {
            log.warn("Product doesn't contains price");
        }
        return hasTitle && hasPrice;
    }

    private void saveToMap(Product product) {

        long count = 1;
        if (productHashMap.containsKey(product.getTitle())) {
            count = productHashMap.get(product.getTitle()).getCount() + 1;
            log.info("Product exists with count: {}", count);
        } else {
            log.info("Product doesn't exists in map, saving new entity in map");
        }
        product.setCount(count);
        productHashMap.put(product.getTitle(), product);
    }

}
