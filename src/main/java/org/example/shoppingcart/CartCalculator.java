package org.example.shoppingcart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CartCalculator {
    public BigDecimal calculateItemTotal(BigDecimal price, int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateCartTotal(List<CartItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(calculateItemTotal(item.price(), item.quantity()));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}

