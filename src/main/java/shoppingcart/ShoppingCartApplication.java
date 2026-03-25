package shoppingcart;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ShoppingCartApplication {
    private final Scanner scanner;
    private final PrintStream out;
    private final LocaleResolver localeResolver;
    private final CartCalculator cartCalculator;

    public ShoppingCartApplication(InputStream input, PrintStream out) {
        this.scanner = new Scanner(new InputStreamReader(input, StandardCharsets.UTF_8));
        this.out = out;
        this.localeResolver = new LocaleResolver();
        this.cartCalculator = new CartCalculator();
    }

    public void run() {
        Locale selectedLocale = askForLanguage();
        ResourceBundle messages = ResourceBundle.getBundle("messages", selectedLocale);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(selectedLocale);

        int itemCount = readPositiveInt(messages.getString("prompt.items.count"), messages.getString("error.invalid.integer"));
        List<CartItem> items = new ArrayList<>();

        for (int i = 1; i <= itemCount; i++) {
            out.println(String.format(messages.getString("label.item.number"), i));
            BigDecimal price = readPositiveDecimal(messages.getString("prompt.item.price"), messages.getString("error.invalid.decimal"));
            int quantity = readPositiveInt(messages.getString("prompt.item.quantity"), messages.getString("error.invalid.integer"));
            CartItem item = new CartItem(price, quantity);
            items.add(item);

            BigDecimal itemTotal = cartCalculator.calculateItemTotal(price, quantity);
            out.println(String.format(messages.getString("result.item.total"), currencyFormat.format(itemTotal)));
        }

        BigDecimal total = cartCalculator.calculateCartTotal(items);
        out.println(String.format(messages.getString("result.cart.total"), currencyFormat.format(total)));
        out.println(messages.getString("message.done"));
    }

    private Locale askForLanguage() {
        out.println("Select language / Valitse kieli / Valj sprak / 言語を選択してください: en, fi, sv, ja");
        String languageCode = scanner.nextLine();
        return localeResolver.resolve(languageCode);
    }

    private int readPositiveInt(String prompt, String errorMessage) {
        while (true) {
            out.print(prompt + " ");
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }
            out.println(errorMessage);
        }
    }

    private BigDecimal readPositiveDecimal(String prompt, String errorMessage) {
        while (true) {
            out.print(prompt + " ");
            String input = scanner.nextLine().trim().replace(',', '.');
            try {
                BigDecimal value = new BigDecimal(input);
                if (value.compareTo(BigDecimal.ZERO) > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }
            out.println(errorMessage);
        }
    }
}

