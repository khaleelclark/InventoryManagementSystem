package src;

import javafx.application.Application;
import javafx.stage.Stage;

public class InventoryManagementSystem extends Application {

    @Override
    public void start(Stage primaryStage) {
        ScreenController controller = new ScreenController(primaryStage);
        controller.activate("home");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
