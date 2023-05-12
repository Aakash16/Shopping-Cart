package com.eq.shoppingcart.exception;

public class ProductListEmptyException extends RuntimeException{

    public ProductListEmptyException(String exception) {
        super(exception);
    }
}
