package com.eq.shoppingcart.exception;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String exception) {
        super(exception);
    }
}
