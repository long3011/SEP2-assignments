package org.example.shoppingcart;

import java.math.BigDecimal;

public record CartItem(BigDecimal price, int quantity) {
}

