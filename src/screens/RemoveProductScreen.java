package src.screens;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import src.Product;
import src.ProductList;
import src.ProductManager;
import src.ScreenController;

public class RemoveProductScreen {
    private final VBox layout;

    /**
     * Khaleel Zindel Clark
     * CEN 3024 - Software Development 1
     * July 5th, 2025
     * RemoveProductScreen.java
     * This class creates the remove product screen and holds
     * all the ui logic for removing products from the inventory.
     */
    public RemoveProductScreen(ScreenController controller) {

        Label title = new Label("Remove Product");

        TableView<Product> productTable = new TableView<>();

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Product, Integer> expectedQuantityColumn = new TableColumn<>("Expected Quantity");
        expectedQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("expectedQuantity"));

        TableColumn<Product, Double> costColumn = new TableColumn<>("Estimated Cost");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedCost"));

        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCategory().getCategoryName()));

        TableColumn<Product, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        productTable.setPrefHeight(300);
        productTable.setPrefWidth(500);
        VBox.setVgrow(productTable, Priority.ALWAYS);
        productTable.getColumns().addAll(nameColumn, quantityColumn, expectedQuantityColumn, costColumn, categoryColumn, locationColumn);

        ProductList productList = ProductManager.getProducts();
        productTable.getItems().setAll(productList);

        Button removeButton = getButton(productTable);

        if (productList.isEmpty()) {
            title.setText("No products found to remove. Add products now!");
            productTable.setVisible(false);
            productTable.setManaged(false);
            removeButton.setVisible(false);
            removeButton.setManaged(false);
        }

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(_ -> controller.activate("home"));

        layout = new VBox(10, title, productTable, removeButton, backButton);
        layout.setStyle("-fx-padding: 20;");
    }

    private static Button getButton(TableView<Product> productTable) {
        Button removeButton = new Button("Remove Product");
        removeButton.setOnAction(_ -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Selection");
                alert.setHeaderText(null);
                alert.setContentText("Please select a product to remove.");
                alert.showAndWait();
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Removal");
            confirmation.setHeaderText("Are you sure you want to remove this product?");
            confirmation.setContentText("Product: " + selected.getName());

            confirmation.showAndWait().ifPresent(response -> {
                if (response.getButtonData().isDefaultButton()) {
                    int result = ProductManager.removeProduct(selected.getName(), ProductManager.getProducts());
                    if (result == 0) {
                        productTable.getItems().setAll(ProductManager.getProducts());
                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setHeaderText("Could not remove product");
                        error.setContentText("Error code: " + result);
                        error.showAndWait();
                    }
                }
            });

        });
        return removeButton;
    }

    public VBox getView() {
        return layout;
    }
}