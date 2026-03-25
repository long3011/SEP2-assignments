package org.example.shoppingcart;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingCartApplicationTest {
    @Test
    void runsFullFlowAndPrintsLocalizedTotal() {
        String input = String.join("\n", "en", "2", "10.50", "2", "1.99", "3") + "\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(output, true, StandardCharsets.UTF_8);

        ShoppingCartApplication app = new ShoppingCartApplication(in, out);
        app.run();

        String text = output.toString(StandardCharsets.UTF_8);
        assertTrue(text.contains("Shopping cart total:"));
        assertTrue(text.contains("26.97"));
    }
}

