package src;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.stream.Stream;


/**
 * Khaleel Zindel Clark
 * CEN 3024 - Software Development 1
 * July 5, 2025
 * ProductManager.java
 * This class creates houses the core logic for the IMS
 */
public class ProductManager {
    private static final ProductList products = new ProductList();

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

    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * method: addProduct
     * parameters: String name, int quantity, int expectedQuantity, double estimatedCost, Category category, String location
     * return: int
     * purpose: this method holds the business logic for adding a product.
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
        products.add(p);
        return ErrorCodes.OK;
    }

    /**
     * method: isValidProductName
     * parameters: String name
     * return: boolean
     * purpose: this method checks user input strings for product names and
     * locations to ensure they are in the correct format for the IMS
     */
    public static int isValidProductName(String name) {
        if (name == null || name.trim().isEmpty()) return ErrorCodes.NAME_EMPTY;
        if (name.length() < 2) return ErrorCodes.NAME_TOO_SHORT;
        if (name.length() > 50) return ErrorCodes.NAME_TOO_LONG;
        if (!name.matches("[a-zA-Z0-9 '\\-().]+")) return ErrorCodes.NAME_INVALID_CHARACTERS;
        return ErrorCodes.OK;
    }

    /**
     * method: isValidQuantity
     * parameters: int quantity
     * return: int
     * purpose: this method verifies user inputted quantity and estimated quantity
     * aligns with the business rules for the application.
     */
    public static int isValidQuantity(int quantity) {
        return (quantity >= 0 && quantity <= 100) ? ErrorCodes.OK : ErrorCodes.QUANTITY_OUT_OF_RANGE;
    }

    /**
     * method: isValidEstimatedCost
     * parameters: double cost
     * return: int
     * purpose: this method verifies user inputted cost aligns with the
     * business rules for the application.
     */
    public static int isValidEstimatedCost(double cost) {
        return cost >= 0 ? ErrorCodes.OK : ErrorCodes.COST_NEGATIVE;
    }


    /**
     * method: printValidationError
     * parameters: none
     * return: void
     * purpose: this method displays error messages based off
     * of corresponding codes defined in the ErrorCodes class.
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
     * method: removeProductByName
     * parameters: String name
     * return: int
     * purpose: this method holds the business logic for removing products
     */
    public static int removeProduct(String name, ProductList productList) {
        if (productList.isEmpty()) {
            return ErrorCodes.NO_PRODUCTS;
        }
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getName().equalsIgnoreCase(name)) {
                productList.remove(i);
                return ErrorCodes.OK;
            }
        }
        return ErrorCodes.NOT_FOUND;
    }

    /**
     * method: getProductByName
     * parameters: String inputName
     * return: Product
     * purpose: this method returns the product with the name that matches a user's input
     */
    public static Product getProductByName(ProductList productList, String inputName) {
        return productList.stream()
                .filter(p -> p.getName().equalsIgnoreCase(inputName))
                .findFirst()
                .orElse(null);
    }

    /**
     * method: updateProductQuantityDirectly
     * parameters: String productName, int newQuantity
     * return: int
     * purpose: this method holds the business logic for updating product quantities.
     */
    public static int updateProductQuantityDirectly(String productName, int newQuantity, ProductList productList) {
        if (!containsProductWithName(productName, productList)) {
            return ErrorCodes.NOT_FOUND;
        }
        for (Product p : productList) {
            if (p.getName().equalsIgnoreCase(productName)) {
                int code = isValidQuantity(newQuantity);
                if (code != ErrorCodes.OK) return code;
                p.setQuantity(newQuantity);
                return ErrorCodes.OK;
            }
        }
        return ErrorCodes.NOT_UPDATED;
    }

    /**
     * method: containsProductWithName
     * parameters: String inputName
     * return: boolean
     * purpose: this method verifies that a product with the name the user
     * inputs exists
     */
    public static boolean containsProductWithName(String inputName, ProductList products) {
        return products.stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(inputName));
    }

    public static ProductList getProducts() {
        return products;
    }

}
