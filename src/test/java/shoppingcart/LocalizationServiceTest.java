package shoppingcart;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalizationServiceTest {
    @Test
    void getLocalizedStrings() {
        LocalizationService service = new LocalizationService();
        Map<String, String> strings = service.getLocalizedStrings("en");
        assertNotNull(strings);
    }
}

