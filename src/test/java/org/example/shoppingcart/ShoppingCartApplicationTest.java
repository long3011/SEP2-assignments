package org.example.shoppingcart;

import org.junit.jupiter.api.Test;
import shoppingcart.LocaleResolver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShoppingCartApplicationTest {
    @Test
    void exposesExpectedSupportedLanguageCodesForGuiSelection() {
        LocaleResolver resolver = new LocaleResolver();
        assertEquals(List.of("en_US", "fi_FI", "sv_SE", "ja_JP", "ar_AR"), resolver.getSupportedLanguageCodes());
    }
}

