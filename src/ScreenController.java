package src;

import javafx.scene.Scene;
import javafx.stage.Stage;
import src.screens.*;

public class ScreenController {
    private final Stage stage;

    public ScreenController(Stage stage) {
        this.stage = stage;
    }

    public void activate(String screenName) {
        switch (screenName.toLowerCase()) {
            case "home":
                stage.setScene(new Scene(new HomeScreen(this).getView(), 600, 400));
                break;
            case "add":
                stage.setScene(new Scene(new AddProductScreen(this).getView(), 600, 600));
                break;
            case "view":
                stage.setScene(new Scene(new ViewProductsScreen(this).getView(), 600, 400));
                break;
            case "update":
                stage.setScene(new Scene(new UpdateProductScreen(this).getView(), 600, 600));
                break;
            case "remove":
                stage.setScene(new Scene(new RemoveProductScreen(this).getView(), 600, 400));
                break;
            case "understocked":
                stage.setScene(new Scene(new UnderstockedProductsScreen(this).getView(), 600, 400));
                break;
            default:
                throw new IllegalArgumentException("Unknown screen: " + screenName);
        }

        stage.setTitle("Inventory Manager - " + screenName.substring(0, 1).toUpperCase() + screenName.substring(1));
        stage.show();
    }
}
