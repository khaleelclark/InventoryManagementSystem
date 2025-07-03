// src/Main.java
package src;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ScreenController controller = new ScreenController(primaryStage);
        controller.activate("home");  // Show the Home screen first
    }

    public static void main(String[] args) {
        launch(args);
    }
}
