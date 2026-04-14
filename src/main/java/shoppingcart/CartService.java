package shoppingcart;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

public class CartService {

    public void saveCart(int totalItems, BigDecimal totalCost, String language, List<CartItem> items) {
        String insertCartSql = "INSERT INTO cart_records (total_items, total_cost, language) VALUES (?, ?, ?)";
        String insertItemSql = "INSERT INTO cart_items (cart_record_id, item_number, price, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement cartStmt = conn.prepareStatement(insertCartSql, Statement.RETURN_GENERATED_KEYS)) {
                cartStmt.setInt(1, totalItems);
                cartStmt.setDouble(2, totalCost.doubleValue());
                cartStmt.setString(3, language);
                cartStmt.executeUpdate();

                int cartRecordId = -1;
                try (ResultSet rs = cartStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cartRecordId = rs.getInt(1);
                    }
                }

                if (cartRecordId != -1) {
                    try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSql)) {
                        int itemNumber = 1;
                        for (CartItem item : items) {
                            itemStmt.setInt(1, cartRecordId);
                            itemStmt.setInt(2, itemNumber++);
                            itemStmt.setDouble(3, item.price().doubleValue());
                            itemStmt.setInt(4, item.quantity());
                            BigDecimal subtotal = item.price().multiply(BigDecimal.valueOf(item.quantity()));
                            itemStmt.setDouble(5, subtotal.doubleValue());
                            itemStmt.addBatch();
                        }
                        itemStmt.executeBatch();
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
            }
        } catch (SQLException e) {
            //do nothing
        }
    }
}

