package src.screens;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import src.*;

import java.io.File;

/**
 * Khaleel Zindel Clark
 * CEN 3024 - Software Development 1
 * July 5th, 2025
 * AddProductScreen.java
 * This class creates the add product screen and holds
 * all the ui logic for adding products to the inventory.
 */
public class AddProductScreen {
    private final VBox layout;

    public AddProductScreen(ScreenController controller) {

        Label title = new Label("Add Product");

        TextField nameField = new TextField();
        TextField quantityField = new TextField();
        TextField expectedQtyField = new TextField();
        TextField costField = new TextField();
        ComboBox<Category> categoryBox = new ComboBox<>();
        TextField locationField = new TextField();

        nameField.setPromptText("Product Name");
        quantityField.setPromptText("Quantity");
        expectedQtyField.setPromptText("Expected Quantity");
        costField.setPromptText("Estimated Cost");
        locationField.setPromptText("Location");
        categoryBox.getItems().addAll(Category.values());
        categoryBox.setPromptText("Select Category");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));
        form.addRow(0, new Label("Name:"), nameField);
        form.addRow(1, new Label("Quantity:"), quantityField);
        form.addRow(2, new Label("Expected Quantity:"), expectedQtyField);
        form.addRow(3, new Label("Estimated Cost:"), costField);
        form.addRow(4, new Label("Category:"), categoryBox);
        form.addRow(5, new Label("Location:"), locationField);

        Button addButton = new Button("Add Product");
        Button importButton = new Button("Import from File");
        Button viewProductsButton = new Button("View Products");
        Button backButton = new Button("Back to Home");

        viewProductsButton.setVisible(false);
        viewProductsButton.setManaged(false);

        addButton.setOnAction(_ -> {
            String name = nameField.getText().trim();
            String quantityText = quantityField.getText().trim();
            String expectedQtyText = expectedQtyField.getText().trim();
            String costText = costField.getText().trim();
            Category category = categoryBox.getValue();
            String location = locationField.getText().trim();

            if (name.isEmpty()) {
                ProductManager.printValidationError(ErrorCodes.NAME_EMPTY);
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException e) {
                ProductManager.showError("Invalid Quantity", "Quantity must be a whole number.");
                return;
            }

            int expectedQty;
            try {
                expectedQty = Integer.parseInt(expectedQtyText);
            } catch (NumberFormatException e) {
                ProductManager.showError("Invalid Expected Quantity", "Expected Quantity must be a whole number.");
                return;
            }

            double cost;
            try {
                cost = Double.parseDouble(costText);
            } catch (NumberFormatException e) {
                ProductManager.showError("Invalid Cost", "Estimated Cost must be a valid number.");
                return;
            }

            if (category == null) {
                ProductManager.showError("Missing Category", "Please select a product category.");
                return;
            }

            if (location.isEmpty()) {
                ProductManager.printValidationError(ErrorCodes.NAME_EMPTY);
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Product Addition");
            confirmation.setHeaderText("Are you sure you want to add this product?");
            confirmation.setContentText("Product: " + name);


            confirmation.showAndWait().ifPresent(response -> {
                if (response.getButtonData().isDefaultButton()) {
                    int result = ProductManager.addProduct(name, quantity, expectedQty, cost, category, location);
                    if (result == ErrorCodes.OK) {
                        ProductManager.showSuccess("Success", "Product added successfully!");

                        nameField.clear();
                        quantityField.clear();
                        expectedQtyField.clear();
                        costField.clear();
                        locationField.clear();
                        categoryBox.getSelectionModel().clearSelection();

                        viewProductsButton.setVisible(true);
                        viewProductsButton.setManaged(true);
                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setHeaderText("Could not add product");
                        error.setContentText("Error code: " + result);
                        error.showAndWait();
                        ProductManager.printValidationError(result);
                    }
                }
            });
        });

        importButton.setOnAction(_ -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Product File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV/TXT", "*.csv", "*.txt"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                boolean success = ProductManager.addProductFromFile(file);
                if (success) {
                    ProductManager.showSuccess("Success", "Product Added Successfully!");
                    viewProductsButton.setVisible(true);
                    viewProductsButton.setManaged(true);
                } else {
                    ProductManager.showError("Import Failed", "Product import failed. Check file format.");
                }
            }
        });

        viewProductsButton.setOnAction(_ -> controller.activate("view"));
        backButton.setOnAction(_ -> controller.activate("home"));

        layout = new VBox(10,
                title,
                form,
                addButton,
                importButton,
                viewProductsButton,
                backButton
        );

        layout.setPadding(new Insets(20));
    }

    public Parent getView() {
        return layout;
    }
}
