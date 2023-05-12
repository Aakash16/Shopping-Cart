package com.eq.shoppingcart.controller;

import com.eq.shoppingcart.exception.CartEmptyException;
import com.eq.shoppingcart.exception.ProductListEmptyException;
import com.eq.shoppingcart.mapper.ProductMapper;
import com.eq.shoppingcart.model.Cart;
import com.eq.shoppingcart.model.Price;
import com.eq.shoppingcart.model.Product;
import com.eq.shoppingcart.model.ProductOut;
import com.eq.shoppingcart.service.CartService;
import com.eq.shoppingcart.service.PriceService;
import com.eq.shoppingcart.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class ShoppingController {

    @Autowired
    private CartService cartService;

    @Autowired
    private PriceService priceService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping(value="/cart")
    public ResponseEntity<List<ProductOut>> addProducts(@RequestBody List<String> productNameList)  {
        log.info("Add Products called with list: {}", productNameList);

        List<Product> productList = productService.getProductList(productNameList);
        log.debug("Product list fetched: {}", productList);

        if(CollectionUtils.isEmpty(productList)){
            throw new ProductListEmptyException("Product list is empty");
        }

        List<Product> productSavedList = cartService.addToCart(productList);
        log.debug("Product list saved: {}", productSavedList);
        List<ProductOut> productOutList = productSavedList.stream().map(p -> productMapper.getProductOut(p)).toList();
        log.debug("Product list returned: {}", productOutList);
        return new ResponseEntity<>(productOutList, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<Cart> getCart(){
        log.info("Get cart called");
        Cart cart = cartService.getCart();
        log.debug("Cart fetched: {}", cart);
        if(ObjectUtils.isEmpty(cart.getProductList()))
            throw new CartEmptyException("Cart is empty");
        return new ResponseEntity<>(cart, HttpStatus.OK);

    }

    @GetMapping("/price")
    public ResponseEntity<Price> getCartPrice(){
        log.info("Get cart price called");
        Price price = priceService.getCartPrice();
        if(ObjectUtils.isEmpty(price))
            throw new CartEmptyException("Cart is empty");

        log.debug("Get price returned: {}", price);
        return new ResponseEntity<>(price, HttpStatus.OK);
    }
}
