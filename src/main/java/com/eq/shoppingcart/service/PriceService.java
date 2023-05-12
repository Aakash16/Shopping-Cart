package com.eq.shoppingcart.service;

import com.eq.shoppingcart.model.Cart;
import com.eq.shoppingcart.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
public class PriceService {

    @Autowired
    CartService cartService;

    @Value("${tax.percent}")
    private double taxPercent;


    public Price getCartPrice() {

        Price price = Price.builder().build();
        Cart cart = cartService.getCart();
        if(ObjectUtils.isEmpty(cart) || ObjectUtils.isEmpty(cart.getProductList())){
            log.error("Cart is empty");
            return price;
        }

       double subTotal = cartService.getCart().getProductList().stream().map(p -> p.getCount() * p.getPrice()).toList().stream().mapToDouble(Double::valueOf).sum();
       double tax = computeTax(subTotal);
       double total = subTotal + tax;

       roundOffAndSetAPrice(price, subTotal, tax, total);
       log.info("Price set:: subTotal: {}, tax: {}, total: {}", price.getSubtotal(), price.getTax(), price.getTotal());
       return price;
    }

    private void roundOffAndSetAPrice(Price price, double subTotal, double tax, double total){
        price.setSubtotal(Math.round(subTotal * 100.0) / 100.0);
        price.setTax(Math.round(tax * 100.0) / 100.0);
        price.setTotal(Math.round(total * 100.0) / 100.0);
    }

    private double computeTax(double subtotal){
        double tax = subtotal * (taxPercent/100);
        return Math.round(tax * 100.0) / 100.0;
    }
}
