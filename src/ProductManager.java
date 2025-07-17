package src;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.stream.Stream;


/**
 * ProductManager.java
 *
 * <p>Core logic handler for the Inventory Management System (IMS).
 * This class provides methods for managing products, including adding,
 * removing, updating, and validating product data.</p>
 *
 * <p>Author: Khaleel Zindel Clark<br>
 * Course: CEN 3024 - Software Development 1<br>
 * Date: July 18, 2025</p>
 */

public class ProductManager {
    private static ProductList products = new ProductList();

    /**
     * <p>Syncs the in-memory ProductList with the contents  the database.</p>
     *
     * <p>This method should be called after a database connection has been successfully established.
     * It ensures that the in-memory data reflects the current state of the database.</p>
     */
    public static void initialize() {
        products = DatabaseManager.loadProducts();
    }

    /**
     * Adds products to the inventory from a file.
     *
     * <p>This method reads a properly formatted <code>.csv</code> or <code>.txt</code> file,
     * validates its contents against business rules, and attempts to add each product to the system.</p>
     *
     * @param file the file containing product data
     * @return {@code true} if at least one product was successfully added; {@code false} otherwise
     */

    public static boolean addProductFromFile(File file) {
        String filePath = file.getAbsolutePath().toLowerCase();
        if (!(filePath.endsWith(".csv") || filePath.endsWith(".txt"))) {
            showError("Invalid File", "Only .csv and .txt files are allowed.");
            return false;
        }

        if (file.length() > 500_000) {
            showError("File Too Large", "Please upload a file smaller than 500KB.");
            return false;
        }

        try (Stream<String> lines = Files.lines(file.toPath())) {
            long lineCount = lines.count();
            if (lineCount > 100) {
                showError("Too Many Products", "File contains more than 100 products.");
                return false;
            }
        } catch (IOException e) {
            showError("Read Error", "Could not read file: " + e.getMessage());
            return false;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            int lineNumber = 1;
            int successCount = 0;

            while (fileScanner.hasNextLine()) {
                String input = fileScanner.nextLine();
                String[] columns = input.split(",");

                if (columns.length != 6) {
                    showError("Too many Columns ", "Line: " + lineNumber + " Expected 6 columns but got " + columns.length);
                    lineNumber++;
                    continue;
                }

                try {
                    String name = columns[0].trim();
                    int quantity = Integer.parseInt(columns[1].trim());
                    int expectedQuantity = Integer.parseInt(columns[2].trim());
                    double estimatedCost = Double.parseDouble(columns[3].trim());
                    Category category = Category.valueOf(columns[4].trim().toUpperCase());
                    String location = columns[5].trim();

                    if (isValidProductName(name) != ErrorCodes.OK ||
                            isValidQuantity(quantity) != ErrorCodes.OK ||
                            isValidQuantity(expectedQuantity) != ErrorCodes.OK ||
                            isValidEstimatedCost(estimatedCost) != ErrorCodes.OK ||
                            isValidProductName(location) != ErrorCodes.OK) {
                        lineNumber++;
                        continue;
                    }

                    int result = addProduct(name, quantity, expectedQuantity, estimatedCost, category, location);
                    if (result == ErrorCodes.OK) {
                        successCount++;
                    }

                } catch (Exception ex) {
                    System.err.println("Line " + lineNumber + ": " + ex.getMessage());
                }
                lineNumber++;
            }
            return successCount > 0;

        } catch (FileNotFoundException e) {
            showError("File Not Found", e.getMessage());
            return false;
        }
    }

    /**
     * Displays an error message to the user using a pop-up alert dialog.
     *
     * <p>This method creates and shows an error alert with a custom title and message.</p>
     *
     * @param title   the title of the error alert
     * @param message the message to be displayed in the alert
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a success message to the user using a pop-up alert dialog.
     *
     * <p>This method creates and shows an informational alert with a custom title and message.</p>
     *
     * @param title   the title of the success alert
     * @param message the message to be displayed in the alert
     */
    public static void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Adds a new product to the database after validating input fields.
     *
     * <p>This method performs input validation for all fields and inserts the product
     * into the database if all validations pass. It then refreshes the in-memory product list.</p>
     *
     * @param name             the name of the product
     * @param quantity         the current quantity of the product
     * @param expectedQuantity the expected quantity to maintain
     * @param estimatedCost    the estimated cost of the product
     * @param category         the category the product belongs to
     * @param location         the storage location of the product
     * @return an error code indicating the result of the operation; {@code ErrorCodes.OK} if successful,
     *         or a specific validation error code otherwise
     */
    public static int addProduct(String name, int quantity, int expectedQuantity, double estimatedCost, Category category, String location) {
        int nameCode = isValidProductName(name);
        if (nameCode != ErrorCodes.OK) return nameCode;

        int quantityCode = isValidQuantity(quantity);
        if (quantityCode != ErrorCodes.OK) return quantityCode;

        int expectedQuantityCode = isValidQuantity(expectedQuantity);
        if (expectedQuantityCode != ErrorCodes.OK) return expectedQuantityCode;

        int costCode = isValidEstimatedCost(estimatedCost);
        if (costCode != ErrorCodes.OK) return costCode;

        Product p = new Product(name, quantity, expectedQuantity, estimatedCost, category, location);
        DatabaseManager.insertProduct(p);
        products = DatabaseManager.loadProducts();
        return ErrorCodes.OK;
    }

    /**
     * Validates a product name to ensure it meets formatting requirements for the IMS.
     *
     * <p>This method checks if the given product name is not null, not empty, has a valid length,
     * and only contains allowed characters (letters, numbers, spaces, hyphens, apostrophes, periods, and parentheses).</p>
     *
     * @param name the product name to validate
     * @return an error code representing the result of the validation:
     *         {@code ErrorCodes.OK} if valid, or a specific error code if invalid
     */
    public static int isValidProductName(String name) {
        if (name == null || name.trim().isEmpty()) return ErrorCodes.NAME_EMPTY;
        if (name.length() < 2) return ErrorCodes.NAME_TOO_SHORT;
        if (name.length() > 50) return ErrorCodes.NAME_TOO_LONG;
        if (!name.matches("[a-zA-Z0-9 '\\-().]+")) return ErrorCodes.NAME_INVALID_CHARACTERS;
        return ErrorCodes.OK;
    }

    /**
     * Validates that a quantity value is within the acceptable range for the IMS.
     *
     * <p>This method ensures the quantity is between 0 and 100, inclusive,
     * according to business rules.</p>
     *
     * @param quantity the quantity value to validate
     * @return {@code ErrorCodes.OK} if the quantity is valid,
     *         or {@code ErrorCodes.QUANTITY_OUT_OF_RANGE} if it falls outside the allowed range
     */
    public static int isValidQuantity(int quantity) {
        return (quantity >= 0 && quantity <= 100) ? ErrorCodes.OK : ErrorCodes.QUANTITY_OUT_OF_RANGE;
    }

    /**
     * Validates that an estimated cost value meets the IMS business rules.
     *
     * <p>This method checks that the cost is not negative, as negative values are not allowed.</p>
     *
     * @param cost the estimated cost value to validate
     * @return {@code ErrorCodes.OK} if the cost is valid,
     *         or {@code ErrorCodes.COST_NEGATIVE} if the cost is less than 0
     */
    public static int isValidEstimatedCost(double cost) {
        return cost >= 0 ? ErrorCodes.OK : ErrorCodes.COST_NEGATIVE;
    }

    /**
     * Displays a user-friendly error message based on a validation error code.
     *
     * <p>This method maps predefined error codes from the {@code ErrorCodes} class to
     * descriptive alert messages and displays them using a pop-up error dialog.</p>
     *
     * @param code the error code representing the validation failure
     */
    public static void printValidationError(int code) {
        switch (code) {
            case ErrorCodes.NAME_EMPTY:
                showError("Name Empty", "Product name cannot be empty.");
                break;
            case ErrorCodes.NAME_TOO_SHORT:
                showError("Name too Short", "Product name must be at least 2 characters long.");
                break;
            case ErrorCodes.NAME_TOO_LONG:
                showError("Name too Long", "Product name must be less than 50 characters.");
                break;
            case ErrorCodes.NAME_INVALID_CHARACTERS:
                showError("Invalid Characters", "Product name contains invalid characters.\n" +
                        "Allowed: letters, numbers, spaces, apostrophes ('), dashes (-), and parentheses.");
                break;
            case ErrorCodes.QUANTITY_OUT_OF_RANGE:
                showError("Quantity out of Range", "Quantity and/or Expected Quantity must be between 0 and 100.");
                break;
            case ErrorCodes.COST_NEGATIVE:
                showError("Negative Cost", "Estimated cost must be a positive number.");
                break;
            case ErrorCodes.NOT_FOUND:
                showError("Not Found", "Error: Name not found.");
                break;
            case ErrorCodes.NOT_UPDATED:
                showError("Not Updated", "Product Not Updated.");
                break;
            default:
                showError("Unknown", "Unknown error. Code: " + code);
        }
    }

    /**
     * Removes a product from the database based on its name.
     *
     * <p>This method checks if the product list is empty, and if not, searches for a product
     * with a matching name (case-insensitive). If found, the product is deleted from the database
     * and the in-memory product list is refreshed.</p>
     *
     * @param name        the name of the product to remove
     * @param productList the current list of products to search through
     * @return {@code ErrorCodes.OK} if the product was successfully removed,
     *         {@code ErrorCodes.NO_PRODUCTS} if the list is empty,
     *         or {@code ErrorCodes.NOT_FOUND} if no matching product was found
     */
    public static int removeProduct(String name, ProductList productList) {
        if (productList.isEmpty()) {
            return ErrorCodes.NO_PRODUCTS;
        }
        for (Product product : productList) {
            if (product.getName().equalsIgnoreCase(name)) {
                DatabaseManager.deleteProduct(name);
                products = DatabaseManager.loadProducts();
                return ErrorCodes.OK;
            }
        }
        return ErrorCodes.NOT_FOUND;
    }

    /**
     * Retrieves a product from the list that matches the given name.
     *
     * <p>This method searches the provided product list for a product whose name matches
     * the user's input (case-insensitive). If a match is found, the product is returned;
     * otherwise, {@code null} is returned.</p>
     *
     * @param productList the list of products to search
     * @param inputName   the name of the product to find
     * @return the matching {@code Product} if found; otherwise {@code null}
     */
    public static Product getProductByName(ProductList productList, String inputName) {
        return productList.stream()
                .filter(p -> p.getName().equalsIgnoreCase(inputName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates all attributes of an existing product in the database.
     *
     * <p>This method performs validation on all provided fields. If validation passes and a product
     * with the specified name exists, it updates the product in the database and refreshes the
     * in-memory product list. Location is validated using the same rules as the product name.</p>
     *
     * @param productName     the name of the product to update
     * @param quantity        the new quantity value
     * @param expectedQuantity the new expected quantity value
     * @param estimatedCost   the new estimated cost value
     * @param category        the new category for the product
     * @param location        the new storage location for the product
     * @param productList     the list of current products to search and update
     * @return {@code ErrorCodes.OK} if the product was successfully updated,
     *         {@code ErrorCodes.NOT_FOUND} if the product was not found,
     *         {@code ErrorCodes.NOT_UPDATED} if the update failed,
     *         or a validation-specific error code for any invalid input
     */
    public static int updateProduct(String productName, int quantity, int expectedQuantity, double estimatedCost, Category category, String location, ProductList productList) {
        if (!containsProductWithName(productName, productList)) {
            return ErrorCodes.NOT_FOUND;
        }

        int nameCode = isValidProductName(productName);
        if (nameCode != ErrorCodes.OK) return nameCode;

        int quantityCode = isValidQuantity(quantity);
        if (quantityCode != ErrorCodes.OK) return quantityCode;

        int expectedCode = isValidQuantity(expectedQuantity);
        if (expectedCode != ErrorCodes.OK) return expectedCode;

        int costCode = isValidEstimatedCost(estimatedCost);
        if (costCode != ErrorCodes.OK) return costCode;

        int locationCode = isValidProductName(location);
        if (locationCode != ErrorCodes.OK) return locationCode;

        for (Product p : productList) {
            if (p.getName().equalsIgnoreCase(productName)) {
                Product updated = new Product(
                        p.getName(),
                        quantity,
                        expectedQuantity,
                        estimatedCost,
                        category,
                        location
                );
                DatabaseManager.updateProduct(updated);
                products = DatabaseManager.loadProducts();
                return ErrorCodes.OK;
            }
        }
        return ErrorCodes.NOT_UPDATED;
    }

    /**
     * Updates the quantity of a specified product directly in the database.
     *
     * <p>This method first checks if a product with the given name exists in the product list.
     * If found, it validates the new quantity and updates the product's quantity in the database,
     * then refreshes the in-memory product list.</p>
     *
     * @param productName the name of the product whose quantity is to be updated
     * @param newQuantity the new quantity value to set
     * @param productList the current list of products to search through
     * @return {@code ErrorCodes.OK} if the quantity was successfully updated,
     *         {@code ErrorCodes.NOT_FOUND} if the product does not exist,
     *         {@code ErrorCodes.NOT_UPDATED} if the update failed,
     *         or a validation-specific error code if the quantity is invalid
     */
    public static int updateProductQuantityDirectly(String productName, int newQuantity, ProductList productList) {
        if (!containsProductWithName(productName, productList)) {
            return ErrorCodes.NOT_FOUND;
        }
        for (Product p : productList) {
            if (p.getName().equalsIgnoreCase(productName)) {
                int code = isValidQuantity(newQuantity);
                if (code != ErrorCodes.OK) return code;
                DatabaseManager.updateQuantity(newQuantity, p.getName());
                products = DatabaseManager.loadProducts();
                return ErrorCodes.OK;
            }
        }
        return ErrorCodes.NOT_UPDATED;
    }

    /**
     * Checks whether a product with the specified name exists in the product list.
     *
     * <p>This method performs a case-insensitive search to determine if any product in the list
     * matches the given name.</p>
     *
     * @param inputName the product name to look for
     * @param products  the list of products to search
     * @return {@code true} if a product with the specified name exists; {@code false} otherwise
     */
    public static boolean containsProductWithName(String inputName, ProductList products) {
        return products.stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(inputName));
    }

    /**
     * Returns a cloned list of products for use by the UI.
     *
     * <p>This method provides a copy of the internal product list to prevent passing by reference instead of value.</p>
     *
     * @return a cloned {@code ProductList} containing all current products
     */
    public static ProductList getProducts() {
        return (ProductList) products.clone();
    }

    /**
     * Checks if the product list is empty.
     *
     * @return {@code true} if there are no products in the list; {@code false} otherwise
     */
    public static boolean isEmpty() {
        return products.isEmpty();
    }

}
