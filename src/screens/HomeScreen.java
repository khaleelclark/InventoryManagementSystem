package src.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import src.ProductManager;
import src.ScreenController;

/**
 * <p>
 * Represents the main menu of the IMS.
 * It provides navigation buttons to the features of the system, including
 * adding, viewing, updating, and removing products, as well as viewing understocked items
 * and estimating total inventory value.
 * </p>
 *
 * <p>
 * This class handles UI logic only and does not contain business logic.
 * </p>
 *
 * @author Khaleel Zindel Clark
 * @version July 18th, 2025
 */
public class HomeScreen {
    private final GridPane layout;

    /**
     * Constructs the HomeScreen and initializes all the UI components.
     *
     * @param controller the ScreenController used to switch between different screens.
     */
    public HomeScreen(ScreenController controller) {
        layout = new GridPane();

        layout.setMinSize(500, 250);
        layout.setPadding(new Insets(20));
        layout.setVgap(15);
        layout.setHgap(15);
        layout.setAlignment(Pos.CENTER);

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
        updateQuantityButton.setOnAction(_ -> controller.activate("quantity"));
        understockedProductsButton.setOnAction(_ -> controller.activate("understocked"));
        calculateInvEstimateButton.setOnAction(_ -> {
            double totalEstimate = ProductManager.getProducts().getTotalInventoryEstimate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inventory Estimate");
            alert.setHeaderText("Total Inventory Value");
            alert.setContentText("Your estimated inventory value is: $" + String.format("%.2f", totalEstimate));
            alert.showAndWait();
        });

        Label description = new Label("Please select from one of the following options below:");
        description.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        description.setPadding(new Insets(10, 10, 10, 10));

        layout.add(description, 0, 0, 2, 1);
        layout.add(addButton, 0, 2);
        layout.add(viewButton, 1, 2);
        layout.add(updateButton, 0, 3);
        layout.add(removeButton, 1, 3);
        layout.add(updateQuantityButton, 0, 4);
        layout.add(understockedProductsButton, 1, 4);
        layout.add(calculateInvEstimateButton, 0, 5, 2, 1);
    }


    /**
     * Returns the JavaFX node representing this screen's layout.
     *
     * @return the layout as a {@link Parent} node.
     */
    public Parent getView() {
        return layout;
    }
}
