package src.screens;

import javafx.scene.Parent;
import src.ProductManager;
import src.ScreenController;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class HomeScreen {
    private ScreenController controller;
    private VBox layout;

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

        addButton.setOnAction(e -> controller.activate("add"));
        viewButton.setOnAction(e -> controller.activate("view"));
        updateButton.setOnAction(e -> controller.activate("update"));
        removeButton.setOnAction(e -> controller.activate("remove"));
        calculateInvEstimateButton.setOnAction(e -> ProductManager.getProducts().getTotalInventoryEstimate());

        layout.getChildren().addAll(addButton, viewButton, updateButton, removeButton, updateQuantityButton, calculateInvEstimateButton, understockedProductsButton);
    }

    public Parent getView() {
        return layout;
    }
}
