package com.eq.shoppingcart.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductOut {
    String title;
    long count;
}
