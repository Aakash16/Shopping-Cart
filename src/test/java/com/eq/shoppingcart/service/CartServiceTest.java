package com.eq.shoppingcart.service;

import com.eq.shoppingcart.model.Cart;
import com.eq.shoppingcart.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Test
    void testAddProduct(){
        List<Product> productList = List.of(Product.builder().title("Cheerios").price(23.4).build(),
                Product.builder().title("Weetabix").price(13.5).build());

        List<Product> productOutList = cartService.addToCart(productList);

        assertEquals(2, productOutList.size());
        assertEquals("Cheerios", productOutList.get(0).getTitle());
        assertEquals(1, productOutList.get(0).getCount());
        assertEquals("Weetabix", productOutList.get(1).getTitle());
        assertEquals(1, productOutList.get(1).getCount());
    }

    @Test
    void testGetCartWithProduct(){
        List<Product> productList = List.of(Product.builder().title("Cheerios").price(23.4).build(),
                Product.builder().title("Weetabix").price(13.5).build());

        cartService.addToCart(productList);
        Cart cart = cartService.getCart();

        assertNotNull(cart);
        assertEquals(2, cart.getProductList().size());
        assertEquals("Cheerios", cart.getProductList().get(0).getTitle());
        assertEquals(1, cart.getProductList().get(0).getCount());
        assertEquals("Weetabix", cart.getProductList().get(1).getTitle());
        assertEquals(1, cart.getProductList().get(1).getCount());
    }

    @Test
    void testCountWhenProductsAddedSubsequently(){
        List<Product> productList1 = List.of(Product.builder().title("Cheerios").price(23.4).build(),
                Product.builder().title("Weetabix").price(13.5).build());

        List<Product> productList2 = List.of(Product.builder().title("Cheerios").price(23.4).build(),
                Product.builder().title("Weetabix").price(13.5).build());

        cartService.addToCart(productList1);
        cartService.addToCart(productList2);
        Cart cart = cartService.getCart();

        assertNotNull(cart);
        assertEquals(2, cart.getProductList().size());
        assertEquals("Cheerios", cart.getProductList().get(0).getTitle());
        assertEquals(2, cart.getProductList().get(0).getCount());
        assertEquals("Weetabix", cart.getProductList().get(1).getTitle());
        assertEquals(2, cart.getProductList().get(1).getCount());
    }

    @Test
    void testGetCartWhenProductIsEmpty(){
        Cart cart = cartService.getCart();
        assertEquals(0, cart.getProductList().size());
    }
}
