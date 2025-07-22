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

/**
 * Main entry point for the Inventory Management System application.
 * Sets up and activates the initial UI screen.
 */
public class InventoryManagementSystem extends Application {

    @Override
    public void start(Stage primaryStage) {
        ScreenController controller = new ScreenController(primaryStage);
        controller.activate("connection");
    }

    /**
     * Called when the application is stopping.
     * Closes the database connection gracefully.
     */
    @Override
    public void stop() {
        DatabaseManager.close();
        System.out.println("App closed, database connection shut down.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
