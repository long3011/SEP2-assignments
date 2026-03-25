package org.example.shoppingcart;

import org.junit.jupiter.api.Test;
import shoppingcart.CartCalculator;
import shoppingcart.CartItem;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartCalculatorTest {
    private final CartCalculator calculator = new CartCalculator();

    @Test
    void calculatesSingleItemTotal() {
        BigDecimal itemTotal = calculator.calculateItemTotal(new BigDecimal("12.50"), 3);
        assertEquals(new BigDecimal("37.50"), itemTotal);
    }

    @Test
    void calculatesCartTotalFromMultipleItems() {
        List<CartItem> items = List.of(
                new CartItem(new BigDecimal("10.00"), 2),
                new CartItem(new BigDecimal("1.99"), 5)
        );

        BigDecimal cartTotal = calculator.calculateCartTotal(items);
        assertEquals(new BigDecimal("29.95"), cartTotal);
    }
}

