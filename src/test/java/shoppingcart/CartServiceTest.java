package shoppingcart;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CartServiceTest {
    @Test
    void canSaveCartSilently() {
        CartService service = new CartService();
        try {
            service.saveCart(1, new BigDecimal("10.0"), "en", List.of(new CartItem(new BigDecimal("10.0"), 1)));
            // Should not throw exceptions even if DB fails
        } catch (Exception e) {
            // Fail the test if any exception is thrown
            throw new RuntimeException("saveCart should not throw exceptions", e);
        }
        assertTrue(true); // If we reach here, the test passes
    }
}

