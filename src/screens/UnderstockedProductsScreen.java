package src.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
 * UnderstockedProductsScreen.java
 * This class creates the understocked products screen and holds
 * all the ui logic for displaying all products understocked in the inventory.
 */
public class UnderstockedProductsScreen {
    private final VBox layout;

    public UnderstockedProductsScreen(ScreenController controller) {

        Label title = new Label("Understocked Products:");

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
        ProductList understocked = ProductManager.getProducts().getUnderstockedProducts();
        if (understocked.isEmpty()) {
            title.setText("No Understocked Products");
            productTable.setVisible(false);
            productTable.setManaged(false);
        } else {
            productTable.getItems().setAll(understocked);
        }

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(_ -> controller.activate("home"));

        layout = new VBox(10,
                title, productTable, backButton);

        layout.setStyle("-fx-padding: 20;");
    }

    public VBox getView() {
        return layout;
    }
}