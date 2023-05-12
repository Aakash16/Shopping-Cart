package com.eq.shoppingcart.mapper;

import com.eq.shoppingcart.model.Product;
import com.eq.shoppingcart.model.ProductOut;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductOut getProductOut(Product product){
        return ProductOut.builder().title(product.getTitle()).count(product.getCount()).build();
    }

}
