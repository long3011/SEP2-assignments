package org.example.shoppingcart;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocaleResolverTest {
    private final LocaleResolver localeResolver = new LocaleResolver();

    @Test
    void resolvesSupportedLocales() {
        assertEquals(Locale.US, localeResolver.resolve("en"));
        assertEquals(Locale.forLanguageTag("fi-FI"), localeResolver.resolve("fi"));
        assertEquals(Locale.forLanguageTag("sv-SE"), localeResolver.resolve("sv"));
        assertEquals(Locale.JAPANESE, localeResolver.resolve("ja"));
    }

    @Test
    void fallsBackToEnglishForUnknownLocale() {
        assertEquals(Locale.US, localeResolver.resolve("xx"));
        assertEquals(Locale.US, localeResolver.resolve(null));
    }
}

