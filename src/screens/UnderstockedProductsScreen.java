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

public class UnderstockedProductsScreen {
    private final VBox layout;

    public UnderstockedProductsScreen(ScreenController controller) {

        Label title = new Label("Understocked Products:");

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

        productTable.setPrefHeight(300);
        productTable.setPrefWidth(500);
        VBox.setVgrow(productTable, Priority.ALWAYS);
        productTable.getColumns().addAll(nameCol, qtyCol, expectedCol, costCol, categoryCol, locationCol);
        ProductList understocked = ProductManager.getProducts().getUnderstockedProducts();
        if (understocked.isEmpty()) {
            title.setText("No Understocked Products");
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