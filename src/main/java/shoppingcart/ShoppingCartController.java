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
import java.util.ResourceBundle;

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

    private Locale currentLocale;
    private ResourceBundle messages;
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
            statusLabel.setText(messages.getString("error.invalid.itemCount"));
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
            statusLabel.setText(messages.getString("error.no.items"));
            return;
        }

        List<CartItem> cartItems = new ArrayList<>();
        for (ItemRow row : itemRows) {
            BigDecimal price = parsePositiveDecimal(row.priceField().getText());
            Integer quantity = parsePositiveInt(row.quantityField().getText());
            if (price == null || quantity == null) {
                statusLabel.setText(messages.getString("error.invalid.row"));
                return;
            }

            BigDecimal itemTotal = cartCalculator.calculateItemTotal(price, quantity);
            row.totalValueLabel().setText(currencyFormat.format(itemTotal));
            cartItems.add(new CartItem(price, quantity));
        }

        BigDecimal overall = cartCalculator.calculateCartTotal(cartItems);
        overallTotalValueLabel.setText(currencyFormat.format(overall));
    }

    private void updateLocale() {
        currentLocale = localeResolver.resolve(languageComboBox.getValue());
        messages = ResourceBundle.getBundle("i18n.MessagesBundle", currentLocale);
        currencyFormat = NumberFormat.getCurrencyInstance(currentLocale);

        languageLabel.setText(messages.getString("label.language"));
        itemCountLabel.setText(messages.getString("label.itemCount"));
        itemCountField.setPromptText(messages.getString("prompt.itemCount"));
        generateRowsButton.setText(messages.getString("button.generateRows"));
        calculateButton.setText(messages.getString("button.calculate"));
        overallTotalTitleLabel.setText(messages.getString("label.overallTotal"));

        root.setNodeOrientation(isArabic() ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);
    }

    private void refreshItemRowLabels() {
        for (int i = 0; i < itemRows.size(); i++) {
            ItemRow row = itemRows.get(i);
            row.itemLabel().setText(String.format(messages.getString("label.itemNumber"), i + 1));
            row.priceLabel().setText(messages.getString("label.price"));
            row.quantityLabel().setText(messages.getString("label.quantity"));
            row.totalTitleLabel().setText(messages.getString("label.itemTotal"));
            row.priceField().setPromptText(messages.getString("prompt.price"));
            row.quantityField().setPromptText(messages.getString("prompt.quantity"));
        }
    }

    private ItemRow createItemRow(int index) {
        Label itemLabel = new Label(String.format(messages.getString("label.itemNumber"), index));
        Label priceLabel = new Label(messages.getString("label.price"));
        TextField priceField = new TextField();
        priceField.setPromptText(messages.getString("prompt.price"));

        Label quantityLabel = new Label(messages.getString("label.quantity"));
        TextField quantityField = new TextField();
        quantityField.setPromptText(messages.getString("prompt.quantity"));

        Label totalTitleLabel = new Label(messages.getString("label.itemTotal"));
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


