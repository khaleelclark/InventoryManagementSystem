package src.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import src.ScreenController;

public class UpdateProductScreen {
    private ScreenController controller;
    private VBox layout;

    public UpdateProductScreen(ScreenController controller) {
        this.controller = controller;
        Label title = new Label("Update Product");

        Label description = new Label("This is the update product screen");

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> controller.activate("home"));

        layout = new VBox(10, title, description, backButton);
        layout.setStyle("-fx-padding: 20;");
    }

    public VBox getView() {
        return layout;
    }
}
