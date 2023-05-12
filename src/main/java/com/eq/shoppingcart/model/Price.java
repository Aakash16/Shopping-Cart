package com.eq.shoppingcart.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Price {

    double subtotal;
    double tax;
    double total;
}
