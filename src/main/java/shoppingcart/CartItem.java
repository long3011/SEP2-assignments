package shoppingcart;

import java.math.BigDecimal;

public record CartItem(BigDecimal price, int quantity) {
}

