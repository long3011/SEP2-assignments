package shoppingcart;

import java.util.Locale;
import java.util.Map;

public class LocaleResolver {
    private static final Locale DEFAULT_LOCALE = Locale.US;
    private static final Map<String, Locale> SUPPORTED_LOCALES = Map.of(
            "en", Locale.US,
            "fi", Locale.forLanguageTag("fi-FI"),
            "sv", Locale.forLanguageTag("sv-SE"),
            "ja", Locale.JAPANESE
    );

    public Locale resolve(String languageCode) {
        if (languageCode == null) {
            return DEFAULT_LOCALE;
        }
        String key = languageCode.trim().toLowerCase(Locale.ROOT);
        return SUPPORTED_LOCALES.getOrDefault(key, DEFAULT_LOCALE);
    }
}