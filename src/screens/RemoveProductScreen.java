package src.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import src.ScreenController;

public class RemoveProductScreen {
    private ScreenController controller;
    private final VBox layout;

    public RemoveProductScreen(ScreenController controller) {
        this.controller = controller;
        Label title = new Label("Remove Product");

        Label description = new Label("This is the Remove product screen");

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(_ -> controller.activate("home"));

        layout = new VBox(10, title, description, backButton);
        layout.setStyle("-fx-padding: 20;");
    }

    public VBox getView() {
        return layout;
    }
}