package src.screens;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import src.ProductManager;
import src.ScreenController;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class HomeScreen {
    private ScreenController controller;
    private final VBox layout;

    public HomeScreen(ScreenController controller) {
        this.controller = controller;
        layout = new VBox(10);

        Button addButton = new Button("Add Product");
        Button viewButton = new Button("View Products");
        Button updateButton = new Button("Update a Product");
        Button removeButton = new Button("Remove a Product");
        Button updateQuantityButton = new Button("Update Quantity Directly");
        Button calculateInvEstimateButton = new Button("Calculate Inventory Estimate");
        Button understockedProductsButton = new Button("Understocked Products");

        addButton.setOnAction(_ -> controller.activate("add"));
        viewButton.setOnAction(_ -> controller.activate("view"));
        updateButton.setOnAction(_ -> controller.activate("update"));
        removeButton.setOnAction(_ -> controller.activate("remove"));
        understockedProductsButton.setOnAction(_ -> controller.activate("understocked"));
        updateQuantityButton.setOnAction(_ -> controller.activate("quantity"));
        calculateInvEstimateButton.setOnAction(_ -> {

            double totalEstimate = ProductManager.getProducts().getTotalInventoryEstimate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inventory Estimate");
            alert.setHeaderText("Total Inventory Value");
            alert.setContentText("Your estimated inventory value is: $" + String.format("%.2f", totalEstimate));

            alert.showAndWait();
        });

        layout.getChildren().addAll(addButton, viewButton, updateButton, removeButton, updateQuantityButton, understockedProductsButton, calculateInvEstimateButton);
    }

    public Parent getView() {
        return layout;
    }
}
