package shoppingcart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ShoppingCartFxApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Locale defaultLocale = Locale.forLanguageTag("en-US");
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.MessagesBundle", defaultLocale);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cart-view.fxml"), bundle);
        Scene scene = new Scene(loader.load());

        stage.setTitle(bundle.getString("app.title"));
        stage.setScene(scene);
        stage.setMinWidth(860);
        stage.setMinHeight(560);
        stage.show();
    }
}

