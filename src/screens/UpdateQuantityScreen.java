package src.screens;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import src.*;

/**
 * Khaleel Zindel Clark
 * CEN 3024 - Software Development 1
 * July 5th, 2025
 * UpdateQuantityScreen.java
 * This class creates the update quantity screen and holds
 * all the ui logic for updating a products quantity in the inventory.
 */
public class UpdateQuantityScreen {
    private final VBox layout;

    public UpdateQuantityScreen(ScreenController controller) {

        Label title = new Label("Update Product");

        TableView<Product> productTable = new TableView<>();
        productTable.setItems(FXCollections.observableArrayList(ProductManager.getProducts()));
        VBox.setVgrow(productTable, Priority.ALWAYS);

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Product, Integer> expectedCol = new TableColumn<>("Expected Qty");
        expectedCol.setCellValueFactory(new PropertyValueFactory<>("expectedQuantity"));


        productTable.getColumns().addAll(nameCol, qtyCol, expectedCol);
        ProductList products = ProductManager.getProducts();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        TextField nameField = new TextField();
        TextField qtyField = new TextField();
        TextField expectedQtyField = new TextField();

        form.addRow(0, new Label("Name:"), nameField);
        form.addRow(1, new Label("Quantity:"), qtyField);
        form.addRow(2, new Label("Expected Qty:"), expectedQtyField);

        Button updateButton = new Button("Update Product");

        if (products.isEmpty()) {
            title.setText("No products found to Update. Add products now!");
            productTable.setVisible(false);
            productTable.setManaged(false);
            form.setVisible(false);
            form.setManaged(false);
            updateButton.setVisible(false);
            updateButton.setManaged(false);
        }

        productTable.setOnMouseClicked(_ -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                nameField.setText(selected.getName());
                qtyField.setText(String.valueOf(selected.getQuantity()));
                expectedQtyField.setText(String.valueOf(selected.getExpectedQuantity()));
            }
        });
        updateButton.setOnAction(_ -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                ProductManager.showError("No Selection", "Please select a product to update");
                return;
            }

            try {
                String name = nameField.getText().trim();
                if (!name.isBlank() && !name.equals(selected.getName())) {
                    int code = ProductManager.isValidProductName(name);
                    if (code != ErrorCodes.OK) {
                        ProductManager.printValidationError(code);
                        return;
                    }
                    selected.setName(name);
                }

                String qtyStr = qtyField.getText().trim();
                if (!qtyStr.isBlank()) {
                    int qty = Integer.parseInt(qtyStr);
                    if (qty != selected.getQuantity()) {
                        int code = ProductManager.isValidQuantity(qty);
                        if (code != ErrorCodes.OK) {
                            ProductManager.printValidationError(code);
                            return;
                        }
                        selected.setQuantity(qty);
                    }
                }

                String expectedStr = expectedQtyField.getText().trim();
                if (!expectedStr.isBlank()) {
                    int expected = Integer.parseInt(expectedStr);
                    if (expected != selected.getExpectedQuantity()) {
                        int code = ProductManager.isValidQuantity(expected);
                        if (code != ErrorCodes.OK) {
                            ProductManager.printValidationError(code);
                            return;
                        }
                        selected.setExpectedQuantity(expected);
                    }
                }

                productTable.getItems().setAll(products);
                productTable.refresh();

                ProductManager.showSuccess("Success", "Product updated!");

            } catch (NumberFormatException e) {
                ProductManager.showError("Input Error", "Quantity and Expected Quantity must be whole numbers.");
            } catch (Exception ex) {
                ProductManager.showError("Invalid input", "Please check fields.");
            }
        });

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(_ -> controller.activate("home"));

        layout = new VBox(10, title, productTable, form, updateButton, backButton);
        layout.setPadding(new Insets(20));
        VBox.setVgrow(productTable, Priority.ALWAYS);
    }

    public VBox getView() {
        return layout;
    }
}
