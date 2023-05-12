package com.eq.shoppingcart.exception;

public class CartEmptyException extends RuntimeException{

    public CartEmptyException(String exception) {
        super(exception);
    }
}
