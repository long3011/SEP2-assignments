package shoppingcart;

import java.util.Locale;
import java.util.List;
import java.util.Map;

public class LocaleResolver {
    private static final Locale DEFAULT_LOCALE = Locale.forLanguageTag("en-US");
    private static final Map<String, Locale> SUPPORTED_LOCALES = Map.of(
            "en", Locale.forLanguageTag("en-US"),
            "en_us", Locale.forLanguageTag("en-US"),
            "fi", Locale.forLanguageTag("fi-FI"),
            "fi_fi", Locale.forLanguageTag("fi-FI"),
            "sv", Locale.forLanguageTag("sv-SE"),
            "sv_se", Locale.forLanguageTag("sv-SE"),
            "ja", Locale.forLanguageTag("ja-JP"),
            "ja_jp", Locale.forLanguageTag("ja-JP"),
            "ar", Locale.forLanguageTag("ar-AR"),
            "ar_ar", Locale.forLanguageTag("ar-AR")
    );

    private static final List<String> SUPPORTED_LANGUAGE_CODES = List.of("en_US", "fi_FI", "sv_SE", "ja_JP", "ar_AR");

    public Locale resolve(String languageCode) {
        if (languageCode == null) {
            return DEFAULT_LOCALE;
        }
        String key = languageCode.trim().replace('-', '_').toLowerCase(Locale.ROOT);
        return SUPPORTED_LOCALES.getOrDefault(key, DEFAULT_LOCALE);
    }

    public List<String> getSupportedLanguageCodes() {
        return SUPPORTED_LANGUAGE_CODES;
    }
}