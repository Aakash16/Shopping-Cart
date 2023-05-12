package com.eq.shoppingcart.service;

import com.eq.shoppingcart.client.ProductRestClient;
import com.eq.shoppingcart.exception.InvalidProductException;
import com.eq.shoppingcart.exception.ProductNotFoundException;
import com.eq.shoppingcart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRestClient productRestClient;

    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(productService, "cheeriosInfoUrl", "https://equalexperts.github.io/backend-take-home-test-data/cheerios.json");
        ReflectionTestUtils.setField(productService, "cornflakesInfoUrl", "https://equalexperts.github.io/backend-take-home-test-data/cornflakes.json");
        ReflectionTestUtils.setField(productService, "frostiesInfoUrl", "https://equalexperts.github.io/backend-take-home-test-data/frosties.json");
        ReflectionTestUtils.setField(productService, "shreddiesInfoUrl", "https://equalexperts.github.io/backend-take-home-test-data/shreddies.json");
        ReflectionTestUtils.setField(productService, "weetabixInfoUrl", "https://equalexperts.github.io/backend-take-home-test-data/weetabix.json");
    }


    @ParameterizedTest()
    @ValueSource(strings = {"Cheerios","Cornflakes","Frosties","Shreddies", "Weetabix"})
    void testGetProduct(String productName) {
        when(productRestClient.getProductInfo(anyString())).thenReturn(getProductResponse());
        List<String> productNameList = List.of(productName);

        List<Product> productList = productService.getProductList(productNameList);

        assertNotNull(productList);
        verify(productRestClient, times(1)).getProductInfo(anyString());
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenProductNotFoud(){
        assertThrows( ProductNotFoundException.class, () -> productService.getProductList(List.of("Unkown")));
    }

    @Test
    void shouldThrowInvalidProductExceptionWhenProductNameIsInvalid(){
        assertThrows( InvalidProductException.class, () -> productService.getProductList(List.of("")));
    }

    @Test
    void shouldThrowJsonProcessingExceptionWhenProductNotFoud(){
        when(productRestClient.getProductInfo(anyString())).thenReturn(getIncorrectProductResponse());
        assertThrows( ProductNotFoundException.class, () -> productService.getProductList(List.of("cheerios")));
        verify(productRestClient, times(1)).getProductInfo(anyString());
    }

    private ResponseEntity<String> getProductResponse() {
        return new ResponseEntity<>("""
                {
                  "title": "Cheerios",
                  "price": 8.43
                }""", HttpStatus.OK);
    }

    private ResponseEntity<String> getIncorrectProductResponse() {
        return new ResponseEntity<>("""
                {
                  "someTag": "someValue"
                }""", HttpStatus.OK);
    }
}
