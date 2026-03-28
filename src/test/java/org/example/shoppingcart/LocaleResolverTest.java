package org.example.shoppingcart;

import org.junit.jupiter.api.Test;
import shoppingcart.LocaleResolver;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocaleResolverTest {
    private final LocaleResolver localeResolver = new LocaleResolver();

    @Test
    void resolvesSupportedLocales() {
        assertEquals(Locale.forLanguageTag("en-US"), localeResolver.resolve("en"));
        assertEquals(Locale.forLanguageTag("en-US"), localeResolver.resolve("en_US"));
        assertEquals(Locale.forLanguageTag("fi-FI"), localeResolver.resolve("fi"));
        assertEquals(Locale.forLanguageTag("fi-FI"), localeResolver.resolve("fi_FI"));
        assertEquals(Locale.forLanguageTag("sv-SE"), localeResolver.resolve("sv"));
        assertEquals(Locale.forLanguageTag("sv-SE"), localeResolver.resolve("sv_SE"));
        assertEquals(Locale.forLanguageTag("ja-JP"), localeResolver.resolve("ja"));
        assertEquals(Locale.forLanguageTag("ja-JP"), localeResolver.resolve("ja_JP"));
        assertEquals(Locale.forLanguageTag("ar-AR"), localeResolver.resolve("ar"));
        assertEquals(Locale.forLanguageTag("ar-AR"), localeResolver.resolve("ar_AR"));
    }

    @Test
    void fallsBackToEnglishForUnknownLocale() {
        assertEquals(Locale.forLanguageTag("en-US"), localeResolver.resolve("xx"));
        assertEquals(Locale.forLanguageTag("en-US"), localeResolver.resolve(null));
    }
}

