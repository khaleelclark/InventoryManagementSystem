package src;

import java.io.File;
import java.sql.*;

/**
 * Manages the SQLite database connection and operations related to
 * loading, inserting, updating, and deleting products in the inventory database.
 * <p>
 * This class handles the database connection and executes SQL queries
 * to interact with the products table.
 * </p>
 *
 * @author Khaleel Zindel Clark
 * @version July 18, 2025
 */
public class DatabaseManager {
    private static Connection connection;

    /**
     * Establishes a connection to the SQLite database at the specified file path.
     * Validates that the file exists and is a proper ".db" file.
     *
     * @param userFilePath the file path to the SQLite database file
     * @return true if the connection was successfully established; false otherwise
     */
    public static boolean establishConnection(String userFilePath) {
        String connectionString = "jdbc:sqlite:" + userFilePath;

        File file = new File(userFilePath);
        if (!file.exists() || file.isDirectory()) {
            ProductManager.showError("Invalid File Path", "The specified path does not exist or is a folder.\n" + userFilePath);
            return false;
        }

        if (!userFilePath.endsWith(".db")) {
            ProductManager.showError("Invalid File Type", "Please select a .db SQLite file.\n" + userFilePath);
            return false;
        }
        try {
            connection = DriverManager.getConnection(connectionString);
            ProductManager.showSuccess("Connection Successful", "Successfully connected to database.");
            ProductManager.initialize();
            return true;
        } catch (SQLException e) {
            ProductManager.showError("Connection Error", e.getMessage() + "\nPlease check your file path and try again." + "\n" + connectionString);
            return false;
        }
    }

    /**
     * Loads all products from the database into a ProductList.
     *
     * @return a ProductList containing all products currently in the database
     */
    public static ProductList loadProducts() {
        String query = "SELECT * FROM products;";
        ProductList dbProductList = new ProductList();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                int expectedQuantity = resultSet.getInt("expected_quantity");
                double estimatedCost = resultSet.getDouble("estimated_cost");
                String categoryStr = resultSet.getString("category");
                Category category = Category.valueOf(categoryStr);
                String location = resultSet.getString("location");

                dbProductList.add(new Product(name, quantity, expectedQuantity, estimatedCost, category, location));
            }
        } catch (SQLException e) {
            ProductManager.showError("Database Error", e.getMessage());
        }
        return dbProductList;
    }

    /**
     * Inserts a new product record into the database.
     *
     * @param product the Product object to insert into the database
     */
    public static void insertProduct(Product product) {
        String query = "INSERT INTO products (name, quantity, expected_quantity, estimated_cost, category, location) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setInt(2, product.getQuantity());
            statement.setInt(3, product.getExpectedQuantity());
            statement.setDouble(4, product.getEstimatedCost());
            statement.setString(5, product.getCategory().name());
            statement.setString(6, product.getLocation());
            statement.executeUpdate();
        } catch (SQLException e) {
            ProductManager.showError("Database Error", e.getMessage());
        }
    }

    /**
     * Updates all attributes of an existing product in the database.
     *
     * @param product the Product object with updated data
     */
    public static void updateProduct(Product product) {
        String query = """
                UPDATE products
                SET quantity = ?,
                    expected_quantity = ?,
                    estimated_cost = ?,
                    category = ?,
                    location = ?
                WHERE name = ?;
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, product.getQuantity());
            statement.setInt(2, product.getExpectedQuantity());
            statement.setDouble(3, product.getEstimatedCost());
            statement.setString(4, product.getCategory().name());
            statement.setString(5, product.getLocation());
            statement.setString(6, product.getName());
            statement.executeUpdate();

        } catch (SQLException e) {
            ProductManager.showError("Database Error", e.getMessage());
        }
    }

    /**
     * Updates the quantity of a product in the database.
     *
     * @param quantity the new quantity to set
     * @param name     the name of the product to update
     */
    public static void updateQuantity(int quantity, String name) {
        String query = "UPDATE products SET quantity = ? WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quantity);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            ProductManager.showError("Database Error", e.getMessage());
        }
    }

    /**
     * Deletes a product from the database by its name.
     *
     * @param name the name of the product to delete
     */
    public static void deleteProduct(String name) {
        String query = "DELETE FROM products WHERE name = ?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            ProductManager.showError("Database Error", e.getMessage());
        }
    }


    /**
     * Closes the database connection if it is open.
     * Should be called when the application is shutting down.
     */
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            ProductManager.showError("Error Closing Connection", e.getMessage());
        }
    }
}
