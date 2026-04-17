package shoppingcart;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class DatabaseConnectionTest {
    @Test
    void attemptConnection() {
        try {
            DatabaseConnection.getConnection();
        } catch (SQLException e) {
            fail(); //fail the test if there's error with connection
        }
        assertTrue(true); // If we reach here, the test passes
    }
}

