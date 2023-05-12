package com.eq.shoppingcart.service;

import com.eq.shoppingcart.model.Cart;
import com.eq.shoppingcart.model.Price;
import com.eq.shoppingcart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTest {

    @InjectMocks
    private PriceService priceService;

    @Mock
    private CartService cartService;

    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(priceService, "taxPercent", 12.5);
    }

    @Test
    void testGetCartPrice(){
        when(cartService.getCart()).thenReturn(
                Cart.builder().productList(
                        List.of(
                                Product.builder()
                                        .title("Cheerios")
                                        .count(2)
                                        .price(23.45)
                                        .build(),
                                Product.builder()
                                        .title("Cornflakes")
                                        .count(3)
                                        .price(43.15)
                                        .build()
                        )
                ).build());

        Price price = priceService.getCartPrice();

        assertEquals(176.35, price.getSubtotal());
        assertEquals(22.04, price.getTax());
        assertEquals(198.39, price.getTotal());
        verify(cartService,times(2)).getCart();


    }

    @Test
    void shouldReturnEmptyPriceWhenCartIsEmpty(){
        when(cartService.getCart()).thenReturn(Cart.builder().build());

        Price price = priceService.getCartPrice();

        assertEquals(0, price.getSubtotal());
        assertEquals(0, price.getTax());
        assertEquals(0, price.getTotal());
        verify(cartService,times(1)).getCart();

    }
}
