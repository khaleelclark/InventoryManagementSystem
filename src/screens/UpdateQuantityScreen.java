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
 * Represents the UI screen for updating the quantity of products in the inventory.
 * <p>
 * This class creates a JavaFX layout that displays a table of products with their current quantities,
 * and a form that allows the user to select a product and update its quantity.
 * It includes validation and error handling for input quantity updates,
 * and a button to return to the home screen.
 * </p>
 *
 * @author Khaleel Zindel Clark
 * @version July 18th, 2025
 */
public class UpdateQuantityScreen {
    private final VBox layout;

    /**
     * Constructs the update quantity screen with a given ScreenController.
     * Initializes UI elements including a table of products, a form for updating quantity,
     * and buttons for submitting changes or returning home.
     *
     * @param controller the ScreenController used to switch screens
     */
    public UpdateQuantityScreen(ScreenController controller) {
        Label title = new Label("Update Product");

        TableView<Product> productTable = new TableView<>();
        productTable.setItems(FXCollections.observableArrayList(ProductManager.getProducts()));
        VBox.setVgrow(productTable, Priority.ALWAYS);

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        productTable.getColumns().addAll(nameColumn, quantityColumn);
        ProductList products = ProductManager.getProducts();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        Label name = new Label("");
        TextField quantityField = new TextField();

        form.addRow(0, new Label("Product Name:"), name);
        form.addRow(1, new Label("Quantity:"), quantityField);

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
                name.setText(selected.getName());
                quantityField.setText(String.valueOf(selected.getQuantity()));
            }
        });
        updateButton.setOnAction(_ -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                ProductManager.showError("No Selection", "Please select a product to update");
                return;
            }

            try {
                String quantityString = quantityField.getText().trim();
                if (!quantityString.isBlank()) {
                    int quantity = Integer.parseInt(quantityString);
                    if (quantity != selected.getQuantity()) {
                        int code = ProductManager.isValidQuantity(quantity);
                        if (code != ErrorCodes.OK) {
                            ProductManager.printValidationError(code);
                            return;
                        }
                        ProductManager.updateProductQuantityDirectly(selected.getName(), quantity, products);
                        ProductList updatedProducts = DatabaseManager.loadProducts();
                        productTable.getItems().setAll(updatedProducts);
                        productTable.refresh();
                    }
                }
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
    /**
     * Returns the root node of this screen's UI layout.
     *
     * @return the VBox containing the update quantity screen layout
     */
    public VBox getView() {
        return layout;
    }
}
