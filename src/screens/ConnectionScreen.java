package src.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import src.DatabaseManager;
import src.ScreenController;

/**
 * ConnectionScreen is the first screen users see.
 * It prompts them to enter the path to their SQLite database file and attempts to establish a connection.
 * If successful, it redirects the user to the home screen.
 *
 * @author Khaleel Zindel Clark
 * @version July 18th, 2025
 */
public class ConnectionScreen {
    private final GridPane layout;

    /**
     * Constructs a new ConnectionScreen.
     *
     * @param controller The ScreenController that handles switching between screens.
     */
    public ConnectionScreen(ScreenController controller) {
        layout = new GridPane();

        layout.setMinSize(500, 250);
        layout.setPadding(new Insets(20));
        layout.setVgap(15);
        layout.setHgap(15);
        layout.setAlignment(Pos.CENTER);

        Button establishConnectionButton = new Button("Establish Connection");
        TextField establishConnectionTextField = new TextField();

        establishConnectionButton.setOnAction(_ -> {
            if (DatabaseManager.establishConnection(establishConnectionTextField.getText())) {
                controller.activate("home");
            } else {
                establishConnectionTextField.clear();
            }
        });

        Label title = new Label("Welcome to Zindel's Inventory Management System");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label description = new Label("Please enter your SQLite file path below:");
        description.setStyle("-fx-font-weight: bold;");
        description.setPadding(new Insets(10, 10, 10, 10));

        Label path = new Label("SQLite file path:");

        establishConnectionTextField.setPromptText("Example: C:/sqlite/InventoryManagementSystem.db");

        layout.add(title, 0, 0, 2, 1);
        layout.add(description, 0, 1, 2, 1);
        layout.add(path, 0, 2, 2, 1);
        layout.add(establishConnectionTextField, 0, 3, 2, 1);
        layout.add(establishConnectionButton, 0, 4, 2, 1);
    }

    /**
     * Returns the JavaFX Parent node representing this screen's layout.
     *
     * @return the layout for this screen
     */
    public Parent getView() {
        return layout;
    }
}
