# Shopping Cart & Localization Java Application

This project is a Java-based shopping cart application that utilizes MySQL/MariaDB for storing localized strings and shopping cart records.

## Prerequisites
- Java Development Kit (JDK) 11 or higher
- Maven
- MySQL or MariaDB server

## Setup Instructions

### 1. Database Setup
1. Ensure your MySQL or MariaDB server is running.
2. Execute the provided `db.sql` script to create the necessary database and tables:
   ```bash
   mysql -u root -p < db.sql
   ```
   This script creates:
   - `shopping_cart_localization` database.
   - `cart_records` table to store main cart information (total items, total cost, language).
   - `cart_items` table linked to `cart_records` with a foreign key constraint.
   - `localization_strings` table to store localized UI strings.

### 2. Application Configuration
Ensure your database credentials (URL, username, password) are properly configured in the `DatabaseConnection` class inside the application `src/main/java/shoppingcart/DatabaseConnection.java`.

### 3. Build the Project
Use Maven to clean and build the project:
```bash
mvn clean install
```

### 4. Run the Application
You can run the application using Maven or directly via the `Main` class.

## Features
- **DatabaseConnection Management:** Handles the connection to the MySQL/MariaDB database.
- **CartService:** Connects to the database to store main cart records and individual items.
- **LocalizationService:** Fetches localized UI strings from the `localization_strings` table based on the selected language, replacing local resource bundles.
- **ShoppingCartApp:** Uses `LocalizationService` for UI text and `CartService` to persist checkout records.

