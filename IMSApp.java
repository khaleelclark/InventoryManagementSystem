import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IMSApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Inventory Management System");

        Label title = new Label("Add Product");

        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");

        TextField qtyField = new TextField();
        qtyField.setPromptText("Quantity");

        TextField expectedQtyField = new TextField();
        expectedQtyField.setPromptText("Expected Quantity");

        TextField costField = new TextField();
        costField.setPromptText("Estimated Cost");

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Category.values());
        categoryComboBox.setPromptText("Select Category");

        TextField locationField = new TextField();
        locationField.setPromptText("Location");

        Button addButton = new Button("Add Product");
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
        productTable.getColumns().clear();
        productTable.getColumns().addAll(nameCol, qtyCol, expectedCol, costCol, categoryCol, locationCol);

        addButton.setOnAction(_ -> {
            try {
                String name = nameField.getText();
                int qty = Integer.parseInt(qtyField.getText());
                int expectedQty = Integer.parseInt(expectedQtyField.getText());
                double cost = Double.parseDouble(costField.getText());
                Category selectedCategory = categoryComboBox.getValue();
                String location = locationField.getText();

                if (selectedCategory == null) {
                    resultLabel.setText("Please select a category.");
                    return;
                }

                int result = ProductManager.addProduct(name, qty, expectedQty, cost,
                        selectedCategory, location);

                productTable.getItems().setAll(ProductManager.getProducts());

                if (result == ErrorCodes.OK) {
                    resultLabel.setText("Product added!");
                    nameField.clear();
                    qtyField.clear();
                    expectedQtyField.clear();
                    costField.clear();
                    categoryComboBox.getSelectionModel().clearSelection();
                    locationField.clear();
                } else {
                    resultLabel.setText("Error code: " + result);
                }

            } catch (Exception ex) {
                resultLabel.setText("Invalid input: " + ex.getMessage());
            }
        });

        VBox layout = new VBox(10,
                title, nameField, qtyField, expectedQtyField,
                costField, categoryComboBox, locationField,
                addButton, resultLabel,
                new Label("Current Products:"), productTable);

        layout.setStyle("-fx-padding: 20;");
        stage.setScene(new Scene(layout, 600, 600)); // Made taller for table
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
