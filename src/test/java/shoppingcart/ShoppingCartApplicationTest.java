package shoppingcart;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingCartApplicationTest {
    @Test
    void exposesExpectedSupportedLanguageCodesForGuiSelection() {
        LocaleResolver resolver = new LocaleResolver();
        assertEquals(List.of("en_US", "fi_FI", "sv_SE", "ja_JP", "ar_AR"), resolver.getSupportedLanguageCodes());
    }

    @Test
    void runApplicationInputs() {
        String input = "en\n1\n12.50\n2\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);

        ShoppingCartApplication app = new ShoppingCartApplication(in, out);

        // This will attempt to save to database - may fail but handles SQLException silently.
        app.run();

        String output = outContent.toString();
        // Just verify it prints prompt for language
        assertTrue(output.contains("Select language"));
    }
}
