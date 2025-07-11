package src;

import java.io.File;
import java.sql.*;

public class DatabaseManager {

    private static Connection connection;


    /**
     * method: establishConnection
     * parameters: String userFilePath
     * return: boolean
     * purpose: this method takes in a file path string from the user
     * to establish a connection with the sqlite database.
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
     * method: loadProducts
     * parameters: none
     * return: ProductList
     * purpose: this method loads in any products in the database.
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
     * method: insertProduct
     * parameters: Product product
     * return: void
     * purpose: this method allows for products to be added
     * to the sqlite database once connection is established.
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
     * method: updateProduct
     * parameters: Product product
     * return: void
     * purpose: this method allows for products to be updated
     * within the sqlite database once connection is established.
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
     * method: updateQuantity
     * parameters: int quantity, String name
     * return: void
     * purpose: this method allows for a products quantity to be updated
     * within the sqlite database once connection is established.
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
     * method: deleteProduct
     * parameters: String name
     * return: void
     * purpose: this method allows for products to be deleted from
     * the sqlite database once connection is established.
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
     * method: close
     * parameters: none
     * return: void
     * purpose: this method allows for the program to
     * end safely by closing teh database connection upon program exit.
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
