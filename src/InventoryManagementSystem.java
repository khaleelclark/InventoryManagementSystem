/**
 * Khaleel Zindel Clark
 * CEN 3024 - Software Development 1
 * July 5, 2025
 * InventoryManagementSystem.java
 * This application will grant Inventory Management System users the ability
 * to add, remove, display, and update products with robust error handling.
 * Users will also be able to calculate the total estimated value of their
 * inventory, and get a list of all products understocked.
 */

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
