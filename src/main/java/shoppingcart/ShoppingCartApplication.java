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
    private final LocalizationService localizationService;
    private final CartService cartService;

    public ShoppingCartApplication(InputStream input, PrintStream out) {
        this.scanner = new Scanner(new InputStreamReader(input, StandardCharsets.UTF_8));
        this.out = out;
        this.localeResolver = new LocaleResolver();
        this.cartCalculator = new CartCalculator();
        this.localizationService = new LocalizationService();
        this.cartService = new CartService();
    }

    public void run() {
        String langCode = askForLanguageStr();
        Locale selectedLocale = localeResolver.resolve(langCode);
        java.util.Map<String, String> messages = localizationService.getLocalizedStrings(langCode);
        if (messages.isEmpty()) {
            ResourceBundle backup = ResourceBundle.getBundle("i18n.MessagesBundle", selectedLocale);
            messages = new java.util.HashMap<>();
            for (String key : backup.keySet()) {
                messages.put(key, backup.getString(key));
            }
        }
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(selectedLocale);

        int itemCount = readPositiveInt(messages.getOrDefault("prompt.items.count", "Number of items?"), messages.getOrDefault("error.invalid.integer", "Invalid int"));
        List<CartItem> items = new ArrayList<>();

        for (int i = 1; i <= itemCount; i++) {
            out.println(String.format(messages.getOrDefault("label.item.number", "Item %d"), i));
            BigDecimal price = readPositiveDecimal(messages.getOrDefault("prompt.item.price", "Price?"), messages.getOrDefault("error.invalid.decimal", "Invalid decimal"));
            int quantity = readPositiveInt(messages.getOrDefault("prompt.item.quantity", "Quantity?"), messages.getOrDefault("error.invalid.integer", "Invalid int"));
            CartItem item = new CartItem(price, quantity);
            items.add(item);

            BigDecimal itemTotal = cartCalculator.calculateItemTotal(price, quantity);
            out.println(String.format(messages.getOrDefault("result.item.total", "Total: %s"), currencyFormat.format(itemTotal)));
        }

        BigDecimal total = cartCalculator.calculateCartTotal(items);
        out.println(String.format(messages.getOrDefault("result.cart.total", "Cart total: %s"), currencyFormat.format(total)));
        out.println(messages.getOrDefault("message.done", "Done"));

        int totalItems = items.stream().mapToInt(CartItem::quantity).sum();
        cartService.saveCart(totalItems, total, langCode, items);
    }

    private Locale askForLanguage() {
        return localeResolver.resolve(askForLanguageStr());
    }

    private String askForLanguageStr() {
        out.println("Select language / Valitse kieli / Valj sprak / 言語を選択してください: en, fi, sv, ja");
        return scanner.nextLine();
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
