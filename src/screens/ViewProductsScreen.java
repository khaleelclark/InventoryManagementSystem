package src.screens;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import src.Category;
import src.Product;
import src.ProductManager;
import src.ScreenController;


public class ViewProductsScreen {
    private ScreenController controller;
    private VBox layout;

    public ViewProductsScreen(ScreenController controller) {
        this.controller = controller;

        Label resultLabel = new Label();

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
        productTable.getItems().setAll(ProductManager.getProducts());

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> controller.activate("home"));

        layout = new VBox(10,
                resultLabel,
                new Label("Current Products:"), productTable, backButton);

        layout.setStyle("-fx-padding: 20;");
    }

    public Parent getView() {
        return layout;
    }
}
