package com.eq.shoppingcart.exception;

public class InvalidProductException extends RuntimeException {
    public InvalidProductException(String exception) {
        super(exception);
    }
}
