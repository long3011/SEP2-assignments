package shoppingcart;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShoppingCartController {
    @FXML
    private VBox root;
    @FXML
    private Label languageLabel;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Label itemCountLabel;
    @FXML
    private TextField itemCountField;
    @FXML
    private Button generateRowsButton;
    @FXML
    private VBox itemsContainer;
    @FXML
    private Button calculateButton;
    @FXML
    private Label overallTotalTitleLabel;
    @FXML
    private Label overallTotalValueLabel;
    @FXML
    private Label statusLabel;

    private final CartCalculator cartCalculator = new CartCalculator();
    private final LocaleResolver localeResolver = new LocaleResolver();
    private final List<ItemRow> itemRows = new ArrayList<>();
    private final LocalizationService localizationService = new LocalizationService();
    private final CartService cartService = new CartService();

    private Locale currentLocale;
    private Map<String, String> messages;
    private NumberFormat currencyFormat;

    @FXML
    public void initialize() {
        languageComboBox.setItems(FXCollections.observableArrayList(localeResolver.getSupportedLanguageCodes()));
        languageComboBox.setValue("en_US");
        updateLocale();
    }

    @FXML
    private void onLanguageChanged() {
        updateLocale();
        refreshItemRowLabels();
        clearStatus();
    }

    @FXML
    private void onGenerateRows() {
        clearStatus();
        Integer itemCount = parsePositiveInt(itemCountField.getText());
        if (itemCount == null) {
            statusLabel.setText(messages.getOrDefault("error.invalid.itemCount", "error.invalid.itemCount"));
            return;
        }

        itemRows.clear();
        itemsContainer.getChildren().clear();
        for (int i = 1; i <= itemCount; i++) {
            ItemRow row = createItemRow(i);
            itemRows.add(row);
            itemsContainer.getChildren().add(row.container());
        }
        overallTotalValueLabel.setText(currencyFormat.format(BigDecimal.ZERO));
    }

    @FXML
    private void onCalculateTotal() {
        clearStatus();
        if (itemRows.isEmpty()) {
            statusLabel.setText(messages.getOrDefault("error.no.items", "error.no.items"));
            return;
        }

        List<CartItem> cartItems = new ArrayList<>();
        for (ItemRow row : itemRows) {
            BigDecimal price = parsePositiveDecimal(row.priceField().getText());
            Integer quantity = parsePositiveInt(row.quantityField().getText());
            if (price == null || quantity == null) {
                statusLabel.setText(messages.getOrDefault("error.invalid.row", "error.invalid.row"));
                return;
            }

            BigDecimal itemTotal = cartCalculator.calculateItemTotal(price, quantity);
            row.totalValueLabel().setText(currencyFormat.format(itemTotal));
            cartItems.add(new CartItem(price, quantity));
        }

        BigDecimal overall = cartCalculator.calculateCartTotal(cartItems);
        overallTotalValueLabel.setText(currencyFormat.format(overall));

        int totalItems = cartItems.stream().mapToInt(CartItem::quantity).sum();
        cartService.saveCart(totalItems, overall, languageComboBox.getValue(), cartItems);
    }

    private void updateLocale() {
        currentLocale = localeResolver.resolve(languageComboBox.getValue());
        messages = localizationService.getLocalizedStrings(languageComboBox.getValue());
        if (messages.isEmpty()) {
            java.util.ResourceBundle backup = java.util.ResourceBundle.getBundle("i18n.MessagesBundle", currentLocale);
            messages = new java.util.HashMap<>();
            for (String key : backup.keySet()) {
                messages.put(key, backup.getString(key));
            }
        }
        currencyFormat = NumberFormat.getCurrencyInstance(currentLocale);

        languageLabel.setText(messages.getOrDefault("label.language", "Language"));
        itemCountLabel.setText(messages.getOrDefault("label.itemCount", "Item Count"));
        itemCountField.setPromptText(messages.getOrDefault("prompt.itemCount", "Item Count"));
        generateRowsButton.setText(messages.getOrDefault("button.generateRows", "Generate Rows"));
        calculateButton.setText(messages.getOrDefault("button.calculate", "Calculate"));
        overallTotalTitleLabel.setText(messages.getOrDefault("label.overallTotal", "Overall Total"));

        root.setNodeOrientation(isArabic() ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);
    }

    private void refreshItemRowLabels() {
        for (int i = 0; i < itemRows.size(); i++) {
            ItemRow row = itemRows.get(i);
            row.itemLabel().setText(String.format(messages.getOrDefault("label.itemNumber", "Item %d"), i + 1));
            row.priceLabel().setText(messages.getOrDefault("label.price", "Price"));
            row.quantityLabel().setText(messages.getOrDefault("label.quantity", "Quantity"));
            row.totalTitleLabel().setText(messages.getOrDefault("label.itemTotal", "Item Total"));
            row.priceField().setPromptText(messages.getOrDefault("prompt.price", "Price"));
            row.quantityField().setPromptText(messages.getOrDefault("prompt.quantity", "Quantity"));
        }
    }

    private ItemRow createItemRow(int index) {
        Label itemLabel = new Label(String.format(messages.getOrDefault("label.itemNumber", "Item %d"), index));
        Label priceLabel = new Label(messages.getOrDefault("label.price", "Price"));
        TextField priceField = new TextField();
        priceField.setPromptText(messages.getOrDefault("prompt.price", "Price"));

        Label quantityLabel = new Label(messages.getOrDefault("label.quantity", "Quantity"));
        TextField quantityField = new TextField();
        quantityField.setPromptText(messages.getOrDefault("prompt.quantity", "Quantity"));

        Label totalTitleLabel = new Label(messages.getOrDefault("label.itemTotal", "Item Total"));
        Label totalValueLabel = new Label(currencyFormat.format(BigDecimal.ZERO));

        HBox container = new HBox(10, itemLabel, priceLabel, priceField, quantityLabel, quantityField, totalTitleLabel, totalValueLabel);
        container.getStyleClass().add("item-row");
        return new ItemRow(container, itemLabel, priceLabel, priceField, quantityLabel, quantityField, totalTitleLabel, totalValueLabel);
    }

    private Integer parsePositiveInt(String text) {
        if (text == null) {
            return null;
        }
        try {
            int value = Integer.parseInt(text.trim());
            return value > 0 ? value : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private BigDecimal parsePositiveDecimal(String text) {
        if (text == null) {
            return null;
        }
        String normalized = text.trim().replace(',', '.');
        try {
            BigDecimal value = new BigDecimal(normalized);
            return value.compareTo(BigDecimal.ZERO) > 0 ? value : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private boolean isArabic() {
        return "ar".equals(currentLocale.getLanguage());
    }

    private void clearStatus() {
        statusLabel.setText("");
    }

    private record ItemRow(
            Node container,
            Label itemLabel,
            Label priceLabel,
            TextField priceField,
            Label quantityLabel,
            TextField quantityField,
            Label totalTitleLabel,
            Label totalValueLabel
    ) {
    }
}
