package src.screens;

import javafx.scene.Parent;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import src.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.File;

public class AddProductScreen {
    private ScreenController controller;
    private final VBox layout;

    public AddProductScreen(ScreenController controller) {
        this.controller = controller;

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
        Button backButton = new Button("Back to Home");
        Button viewProductsButton = new Button("View Products");
        viewProductsButton.setManaged(false);
        viewProductsButton.setVisible(false);

        Label resultLabel = new Label();

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

                int result = ProductManager.addProduct(name, qty, expectedQty, cost, selectedCategory, location);

                if (result == ErrorCodes.OK) {
                    resultLabel.setText("Product added!");
                    nameField.clear();
                    qtyField.clear();
                    expectedQtyField.clear();
                    costField.clear();
                    categoryComboBox.getSelectionModel().clearSelection();
                    locationField.clear();
                    viewProductsButton.setManaged(true);
                    viewProductsButton.setVisible(true);
                } else {
                    resultLabel.setText("Error code: " + result);
                }

            } catch (Exception ex) {
                resultLabel.setText("Invalid input: " + ex.getMessage());
            }
        });

        Button importButton = new Button("Import from File");
        Label importStatus = new Label();


        importButton.setOnAction(_ -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Product File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV and TXT files", "*.csv", "*.txt")
            );

            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                boolean success = ProductManager.addProductFromFile(selectedFile);

                if (success) {
                    importStatus.setText("Products loaded successfully!");
                    viewProductsButton.setVisible(true);
                    viewProductsButton.setManaged(true);
                } else {
                    importStatus.setText("Error: Could not import products. Check file formatting.");
                    viewProductsButton.setVisible(false);
                    viewProductsButton.setManaged(true);
                }
            }
        });


        viewProductsButton.setOnAction(_ -> controller.activate("view"));
        backButton.setOnAction(_ -> controller.activate("home"));

        layout = new VBox(10,
                title, nameField, qtyField, expectedQtyField,
                costField, categoryComboBox, locationField,
                addButton, importButton, importStatus, viewProductsButton, resultLabel, backButton);

        layout.setStyle("-fx-padding: 20;");
    }

    public Parent getView() {
        return layout;
    }
}

