package src.screens;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import src.*;

/**
 * Represents the UI screen for updating product details in the inventory system.
 * <p>
 * This class builds a JavaFX interface that displays all products in a table with
 * columns for product attributes such as name, quantity, expected quantity, estimated cost,
 * category, and location. Users can select a product from the table to populate
 * a form where they can edit its attributes and submit updates.
 * <p>
 * Input validation is performed on product attributes before submission, and
 * appropriate success or error messages are shown.
 * </p>
 * <p>
 * Includes navigation back to the home screen.
 * </p>
 *
 * @author Khaleel Zindel Clark
 * @version July 18th, 2025
 */
public class UpdateProductScreen {
    private final VBox layout;

    /**
     * Constructs the UpdateProductScreen UI.
     * <p>
     * Sets up a product table and a form for editing product details,
     * including text fields for quantity, expected quantity, estimated cost,
     * a dropdown for category selection, and a field for location.
     * Also includes validation of inputs and error handling.
     * </p>
     *
     * @param controller the ScreenController instance used to switch screens
     */
    public UpdateProductScreen(ScreenController controller) {
        Label title = new Label("Update Product");

        TableView<Product> productTable = new TableView<>();
        productTable.setItems(FXCollections.observableArrayList(ProductManager.getProducts()));
        VBox.setVgrow(productTable, Priority.ALWAYS);

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Product, Integer> expectedQuantityColumn = new TableColumn<>("Expected Qty");
        expectedQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("expectedQuantity"));

        TableColumn<Product, Double> costColumn = new TableColumn<>("Est. Cost");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedCost"));

        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory().getCategoryName()));

        TableColumn<Product, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        productTable.getColumns().addAll(nameColumn, quantityColumn, expectedQuantityColumn, costColumn, categoryColumn, locationColumn);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        Label name = new Label();
        TextField quantityField = new TextField();
        TextField expectedQtyField = new TextField();
        TextField costField = new TextField();
        ComboBox<Category> categoryBox = new ComboBox<>(FXCollections.observableArrayList(Category.values()));
        TextField locationField = new TextField();

        form.addRow(0, new Label("Product Name:"), name);
        form.addRow(1, new Label("Quantity:"), quantityField);
        form.addRow(2, new Label("Expected Qty:"), expectedQtyField);
        form.addRow(3, new Label("Est. Cost:"), costField);
        form.addRow(4, new Label("Category:"), categoryBox);
        form.addRow(5, new Label("Location:"), locationField);

        Button updateButton = new Button("Update Product");

        if (ProductManager.isEmpty()) {
            title.setText("No Products to update. Add some now!");
            form.setVisible(false);
            form.setManaged(false);
            productTable.setVisible(false);
            productTable.setManaged(false);
            updateButton.setVisible(false);
            updateButton.setManaged(false);
        }

        productTable.setOnMouseClicked(_ -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                name.setText(selected.getName());
                quantityField.setText(String.valueOf(selected.getQuantity()));
                expectedQtyField.setText(String.valueOf(selected.getExpectedQuantity()));
                costField.setText(String.valueOf(selected.getEstimatedCost()));
                categoryBox.setValue(selected.getCategory());
                locationField.setText(selected.getLocation());
            }
        });

        updateButton.setOnAction(_ -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                ProductManager.showError("No Selection", "Please select a product to update");
                return;
            }
            String productName = selected.getName();

            try {
                String quantityStr = quantityField.getText().trim();
                String expectedStr = expectedQtyField.getText().trim();
                String costStr = costField.getText().trim();
                Category category = categoryBox.getValue();
                String location = locationField.getText().trim();

                int nameCode = ProductManager.isValidProductName(productName);
                int quantityCode = ProductManager.isValidQuantity(Integer.parseInt(quantityStr));
                int expectedCode = ProductManager.isValidQuantity(Integer.parseInt(expectedStr));
                int costCode = ProductManager.isValidEstimatedCost(Double.parseDouble(costStr));
                int locationCode = ProductManager.isValidProductName(location);

                if (nameCode != ErrorCodes.OK) {
                    ProductManager.printValidationError(nameCode);
                    return;
                }
                if (quantityCode != ErrorCodes.OK) {
                    ProductManager.printValidationError(quantityCode);
                    return;
                }
                if (expectedCode != ErrorCodes.OK) {
                    ProductManager.printValidationError(expectedCode);
                    return;
                }
                if (costCode != ErrorCodes.OK) {
                    ProductManager.printValidationError(costCode);
                    return;
                }
                if (locationCode != ErrorCodes.OK) {
                    ProductManager.printValidationError(locationCode);
                    return;
                }

                int code = ProductManager.updateProduct(
                        productName,
                        Integer.parseInt(quantityStr),
                        Integer.parseInt(expectedStr),
                        Double.parseDouble(costStr),
                        category,
                        location,
                        ProductManager.getProducts()
                );

                if (code != ErrorCodes.OK) {
                    ProductManager.printValidationError(code);
                    return;
                }

                ProductList updatedProducts = DatabaseManager.loadProducts();
                productTable.getItems().setAll(updatedProducts);
                productTable.refresh();
                ProductManager.showSuccess("Success", "Product updated!");
                quantityField.clear();
                expectedQtyField.clear();
                costField.clear();
                categoryBox.getSelectionModel().clearSelection();
                locationField.clear();
                
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
     * Returns the root container (VBox) of this screen’s layout.
     *
     * @return the VBox layout containing all UI components for this screen
     */
    public VBox getView() {
        return layout;
    }
}
