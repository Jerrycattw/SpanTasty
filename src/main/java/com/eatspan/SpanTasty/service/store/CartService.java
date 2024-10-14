package com.eatspan.SpanTasty.service.store;

import com.eatspan.SpanTasty.entity.store.Cart;
import com.eatspan.SpanTasty.entity.store.Product;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;


@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cart";

    public void addToCart(HttpSession session, Product product, int quantity) {
        Cart cart = getCart(session);
        cart.addItem(product, quantity);
    }

    public void removeFromCart(HttpSession session, Integer productId) {
        Cart cart = getCart(session);
        cart.removeItem(productId);
    }

    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }
}
