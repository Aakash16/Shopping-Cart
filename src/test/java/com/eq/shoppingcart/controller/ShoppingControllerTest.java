package com.eq.shoppingcart.controller;

import com.eq.shoppingcart.exception.CartEmptyException;
import com.eq.shoppingcart.exception.ProductListEmptyException;
import com.eq.shoppingcart.exception.ShoppingExceptionHandler;
import com.eq.shoppingcart.mapper.ProductMapper;
import com.eq.shoppingcart.model.Cart;
import com.eq.shoppingcart.model.Price;
import com.eq.shoppingcart.model.Product;
import com.eq.shoppingcart.model.ProductOut;
import com.eq.shoppingcart.service.CartService;
import com.eq.shoppingcart.service.PriceService;
import com.eq.shoppingcart.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
public class ShoppingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private CartService cartService;

    @Mock
    private PriceService priceService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ShoppingController shoppingController;

    @BeforeEach
    public void setup() {

        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(shoppingController)
                .setControllerAdvice(new ShoppingExceptionHandler())
                .build();
    }

    @Test
    void shouldAddProductListToCart() throws Exception {
        when(productService.getProductList(List.of("productName1"))).thenReturn(List.of(Product.builder().title("productName1").build()));
        when(cartService.addToCart(List.of(Product.builder().title("productName1").build()))).thenReturn(List.of(Product.builder().title("productName1").count(1).build()));
        when(productMapper.getProductOut(Product.builder().title("productName1").count(1).build())).thenReturn(ProductOut.builder().title("productName1").count(1).build());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/cart")
                .accept(MediaType.APPLICATION_JSON)

                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("[{\"title\":\"productName1\",\"count\":1}]", response.getContentAsString());
        verify(productService,times(1)).getProductList(anyList());
        verify(cartService,times(1)).addToCart(anyList());
        verify(productMapper,times(1)).getProductOut(any());

    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenFailedToFetchProductInfo() {

        ProductListEmptyException plee = assertThrows(ProductListEmptyException.class, () -> shoppingController.addProducts(Collections.emptyList()));
        assertEquals("Product list is empty", plee.getMessage());

    }

    @Test
    void shouldGetCart() throws Exception {
        when(cartService.getCart()).thenReturn(Cart.builder().productList(List.of(Product.builder().title("Cheerios").count(1).price(23.45).build())).build());

        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/cart"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("{\"productList\":[{\"count\":1,\"title\":\"Cheerios\",\"price\":23.45}]}", response.getContentAsString());
        verify(cartService,times(1)).getCart();

    }

    @Test
    void shouldThrowNotFoundExceptionWhenCartIsEmpty() throws Exception {
        when(cartService.getCart()).thenReturn(Cart.builder().build());

        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/cart"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertThat(response.getContentAsString()).contains("{\"message\":\"Cart is empty\",\"dateTime\"");
        verify(cartService,times(1)).getCart();

    }

    @Test
    void shouldGetPrice() throws Exception {
        when(priceService.getCartPrice()).thenReturn(Price.builder().subtotal(12.3).tax(3.4).total(15.7).build());

        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/price"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("{\"subtotal\":12.3,\"tax\":3.4,\"total\":15.7}", response.getContentAsString());
        verify(priceService,times(1)).getCartPrice();

    }

    @Test
    void shouldThrowCartEmptyExceptionWhenGetPriceHasEmptyCart() {

        CartEmptyException cartEmptyException = assertThrows(CartEmptyException.class, () -> shoppingController.getCartPrice());
        assertEquals("Cart is empty", cartEmptyException.getMessage());
    }
}
