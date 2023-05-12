package com.eq.shoppingcart.mapper;

import com.eq.shoppingcart.model.Product;
import com.eq.shoppingcart.model.ProductOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProductMapperTest {

    @InjectMocks
    private ProductMapper productMapper;

    @Test
    void shouldMapProductToProductOut(){
        ProductOut productOut = productMapper.getProductOut(Product.builder().title("title").count(1).price(23.4).build());

        assertEquals("title", productOut.getTitle());
        assertEquals(1, productOut.getCount());
    }

}
