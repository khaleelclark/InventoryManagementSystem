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

public class UpdateProductScreen {
    private final VBox layout;

    public UpdateProductScreen(ScreenController controller) {

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

        TableColumn<Product, Double> costCol = new TableColumn<>("Est. Cost");
        costCol.setCellValueFactory(new PropertyValueFactory<>("estimatedCost"));

        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory().getCategoryName()));

        TableColumn<Product, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        productTable.getColumns().addAll(nameCol, qtyCol, expectedCol, costCol, categoryCol, locationCol);
        ProductList products = ProductManager.getProducts();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        TextField nameField = new TextField();
        TextField qtyField = new TextField();
        TextField expectedQtyField = new TextField();
        TextField costField = new TextField();
        ComboBox<Category> categoryBox = new ComboBox<>(FXCollections.observableArrayList(Category.values()));
        TextField locationField = new TextField();

        form.addRow(0, new Label("Name:"), nameField);
        form.addRow(1, new Label("Quantity:"), qtyField);
        form.addRow(2, new Label("Expected Qty:"), expectedQtyField);
        form.addRow(3, new Label("Est. Cost:"), costField);
        form.addRow(4, new Label("Category:"), categoryBox);
        form.addRow(5, new Label("Location:"), locationField);

        Button updateButton = new Button("Update Product");

        if (products.isEmpty()) {
            title.setText("No Products to update. Add some now!");
            form.setVisible(false);
            form.setManaged(false);
            productTable.setVisible(false);
            productTable.setManaged(false);
            updateButton.setVisible(false);
            updateButton.setManaged(false);
        }

        Label status = new Label();

        productTable.setOnMouseClicked(_ -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                nameField.setText(selected.getName());
                qtyField.setText(String.valueOf(selected.getQuantity()));
                expectedQtyField.setText(String.valueOf(selected.getExpectedQuantity()));
                costField.setText(String.valueOf(selected.getEstimatedCost()));
                categoryBox.setValue(selected.getCategory());
                locationField.setText(selected.getLocation());
                status.setText("");
            }
        });


        updateButton.setOnAction(_ -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                status.setText("Please select a product.");
                return;
            }

            try {
                String name = nameField.getText().trim();
                if (!name.isBlank() && !name.equals(selected.getName()) &&
                        ProductManager.isValidProductName(name) == ErrorCodes.OK) {
                    selected.setName(name);
                }

                String qtyStr = qtyField.getText().trim();
                if (!qtyStr.isBlank()) {
                    int qty = Integer.parseInt(qtyStr);
                    if (qty != selected.getQuantity() && ProductManager.isValidQuantity(qty) == ErrorCodes.OK) {
                        selected.setQuantity(qty);
                    }
                }

                String expectedStr = expectedQtyField.getText().trim();
                if (!expectedStr.isBlank()) {
                    int expected = Integer.parseInt(expectedStr);
                    if (expected != selected.getExpectedQuantity() &&
                            ProductManager.isValidQuantity(expected) == ErrorCodes.OK) {
                        selected.setExpectedQuantity(expected);
                    }
                }

                String costStr = costField.getText().trim();
                if (!costStr.isBlank()) {
                    double cost = Double.parseDouble(costStr);
                    if (cost != selected.getEstimatedCost() &&
                            ProductManager.isValidEstimatedCost(cost) == ErrorCodes.OK) {
                        selected.setEstimatedCost(cost);
                    }
                }

                Category category = categoryBox.getValue();
                if (category != null && category != selected.getCategory()) {
                    selected.setCategory(category);
                }

                String location = locationField.getText().trim();
                if (!location.isBlank() && !location.equals(selected.getLocation()) &&
                        ProductManager.isValidProductName(location) == ErrorCodes.OK) {
                    selected.setLocation(location);
                }

                // Refresh table
                productTable.getItems().setAll(ProductManager.getProducts());
                productTable.refresh();

                status.setText("Product updated!");

            } catch (Exception ex) {
                status.setText("Invalid input. Please check fields.");
            }
        });

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(_ -> controller.activate("home"));

        layout = new VBox(10, title, productTable, form, updateButton, status, backButton);
        layout.setPadding(new Insets(20));
        VBox.setVgrow(productTable, Priority.ALWAYS);
    }

    public VBox getView() {
        return layout;
    }
}
