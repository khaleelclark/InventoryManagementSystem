package src.screens;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import src.Product;
import src.ProductList;
import src.ProductManager;
import src.ScreenController;

public class RemoveProductScreen {
    private ScreenController controller;
    private final VBox layout;

//    public RemoveProductScreen(ScreenController controller) {
//        this.controller = controller;
//        Label title = new Label("Remove Product");
//
//        Label description = new Label("This is the Remove product screen");
//
//        Button backButton = new Button("Back to Home");
//        backButton.setOnAction(_ -> controller.activate("home"));
//
//        layout = new VBox(10, title, description, backButton);
//        layout.setStyle("-fx-padding: 20;");
//    }

    public RemoveProductScreen(ScreenController controller) {
        this.controller = controller;

        Label title = new Label("Remove Product");

        TableView<Product> productTable = new TableView<>();

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Product, Integer> expectedCol = new TableColumn<>("Expected Quantity");
        expectedCol.setCellValueFactory(new PropertyValueFactory<>("expectedQuantity"));

        TableColumn<Product, Double> costCol = new TableColumn<>("Estimated Cost");
        costCol.setCellValueFactory(new PropertyValueFactory<>("estimatedCost"));

        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCategory().getCategoryName()));

        TableColumn<Product, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        productTable.setPrefHeight(200);
        productTable.getColumns().addAll(nameCol, qtyCol, expectedCol, costCol, categoryCol, locationCol);

        ProductList productList = ProductManager.getProducts();
        productTable.getItems().setAll(productList);

        Button removeButton = getButton(productTable);

        if (productList.isEmpty()) {
            title.setText("No Products Found. Add products now!");
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
                    int result = ProductManager.removeProductByName(selected.getName(), ProductManager.getProducts());
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