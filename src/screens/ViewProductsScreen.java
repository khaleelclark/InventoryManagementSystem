package src.screens;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import src.Product;
import src.ProductList;
import src.ProductManager;
import src.ScreenController;

/**
 * Khaleel Zindel Clark
 * CEN 3024 - Software Development 1
 * July 5th, 2025
 * ViewProductsScreen.java
 * This class creates the view products screen and holds
 * all the ui logic for viewing products in the inventory.
 */
public class ViewProductsScreen {
    private final VBox layout;

    public ViewProductsScreen(ScreenController controller) {

        Label title = new Label("All Products: ");

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
        ProductList products = ProductManager.getProducts();
        productTable.getItems().setAll(products);

        if (products.isEmpty()) {
            title.setText("No Products Found. Add products now!");
            productTable.setVisible(false);
            productTable.setManaged(false);
        }

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(_ -> controller.activate("home"));

        layout = new VBox(10,
                title, productTable, backButton);

        layout.setStyle("-fx-padding: 20;");
    }

    public Parent getView() {
        return layout;
    }
}
