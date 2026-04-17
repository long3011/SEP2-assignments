package shoppingcart;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CartItemTest {
    @Test
    void canAccessProperties() {
        CartItem item = new CartItem(new BigDecimal("10.00"), 5);
        assertEquals(new BigDecimal("10.00"), item.price());
        assertEquals(5, item.quantity());
    }
}

