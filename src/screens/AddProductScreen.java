package src.screens;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import src.*;

import java.io.File;

public class AddProductScreen {
    private final VBox layout;

    public AddProductScreen(ScreenController controller) {

        Label title = new Label("Add Product");

        TextField nameField = new TextField();
        TextField qtyField = new TextField();
        TextField expectedQtyField = new TextField();
        TextField costField = new TextField();
        ComboBox<Category> categoryBox = new ComboBox<>();
        TextField locationField = new TextField();

        nameField.setPromptText("Product Name");
        qtyField.setPromptText("Quantity");
        expectedQtyField.setPromptText("Expected Quantity");
        costField.setPromptText("Estimated Cost");
        locationField.setPromptText("Location");
        categoryBox.getItems().addAll(Category.values());
        categoryBox.setPromptText("Select Category");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));
        form.addRow(0, new Label("Name:"), nameField);
        form.addRow(1, new Label("Quantity:"), qtyField);
        form.addRow(2, new Label("Expected Quantity:"), expectedQtyField);
        form.addRow(3, new Label("Estimated Cost:"), costField);
        form.addRow(4, new Label("Category:"), categoryBox);
        form.addRow(5, new Label("Location:"), locationField);

        Button addButton = new Button("Add Product");
        Button importButton = new Button("Import from File");
        Button viewProductsButton = new Button("View Products");
        Button backButton = new Button("Back to Home");

        viewProductsButton.setVisible(false);
        viewProductsButton.setManaged(false);

        Label resultLabel = new Label();
        Label importStatus = new Label();

        addButton.setOnAction(_ -> {
            try {
                String name = nameField.getText();
                int qty = Integer.parseInt(qtyField.getText());
                int expectedQty = Integer.parseInt(expectedQtyField.getText());
                double cost = Double.parseDouble(costField.getText());
                Category category = categoryBox.getValue();
                String location = locationField.getText();

                if (category == null) {
                    resultLabel.setText("Please select a category.");
                    return;
                }

                int result = ProductManager.addProduct(name, qty, expectedQty, cost, category, location);
                if (result == ErrorCodes.OK) {
                    resultLabel.setText("Product added!");
                    nameField.clear();
                    qtyField.clear();
                    expectedQtyField.clear();
                    costField.clear();
                    locationField.clear();
                    categoryBox.getSelectionModel().clearSelection();

                    viewProductsButton.setVisible(true);
                    viewProductsButton.setManaged(true);
                } else {
                    resultLabel.setText("Error code: " + result);
                }
            } catch (Exception ex) {
                resultLabel.setText("Invalid input: " + ex.getMessage());
            }
        });

        importButton.setOnAction(_ -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Product File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV/TXT", "*.csv", "*.txt"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                boolean success = ProductManager.addProductFromFile(file);
                if (success) {
                    importStatus.setText("Products loaded successfully!");
                    viewProductsButton.setVisible(true);
                    viewProductsButton.setManaged(true);
                } else {
                    importStatus.setText("Failed to import. Check file format.");
                }
            }
        });

        viewProductsButton.setOnAction(_ -> controller.activate("view"));
        backButton.setOnAction(_ -> controller.activate("home"));

        layout = new VBox(10,
                title,
                form,
                addButton,
                resultLabel,
                importButton,
                importStatus,
                viewProductsButton,
                backButton
        );

        layout.setPadding(new Insets(20));
    }

    public Parent getView() {
        return layout;
    }
}
