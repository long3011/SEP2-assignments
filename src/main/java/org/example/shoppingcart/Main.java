package org.example.shoppingcart;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        ShoppingCartApplication app = new ShoppingCartApplication(System.in, System.out);
        app.run();
    }
}

