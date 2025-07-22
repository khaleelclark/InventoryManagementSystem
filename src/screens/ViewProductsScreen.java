package src.screens;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import src.Product;
import src.ProductList;
import src.ProductManager;
import src.ScreenController;

/**
 * Represents the UI screen for viewing all products in the inventory.
 * <p>
 * This class builds a JavaFX layout displaying a table of all products
 * retrieved from the ProductManager, including columns for name, quantity,
 * expected quantity, estimated cost, category, and location.
 * It also handles UI logic such as showing a message when no products are present,
 * and provides a back button to navigate to the home screen.
 * </p>
 *
 * @author Khaleel Zindel Clark
 * @version July 18th, 2025
 */
public class ViewProductsScreen {
    private final VBox layout;

    /**
     * Constructs the view products screen with a given ScreenController.
     * The screen shows a table listing all products in the inventory.
     *
     * @param controller the ScreenController used to switch screens
     */
    public ViewProductsScreen(ScreenController controller) {
        Label title = new Label("All Products: ");

        TableView<Product> productTable = new TableView<>();

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Product, Integer> expectedQuantityColumn = new TableColumn<>("Expected Quantity");
        expectedQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("expectedQuantity"));

        TableColumn<Product, Double> costColumn = new TableColumn<>("Estimated Cost");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedCost"));

        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCategory().getCategoryName()));

        TableColumn<Product, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        productTable.setPrefHeight(300);
        productTable.setPrefWidth(500);
        VBox.setVgrow(productTable, Priority.ALWAYS);
        productTable.getColumns().addAll(nameColumn, quantityColumn, expectedQuantityColumn, costColumn, categoryColumn, locationColumn);
        ProductList products = ProductManager.getProducts();
        productTable.getItems().setAll(products);

        if (products.isEmpty()) {
            title.setText("No Products Found. Add products now!");
            productTable.setVisible(false);
            productTable.setManaged(false);
        }

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(_ -> controller.activate("home"));

        layout = new VBox(10,
                title, productTable, backButton);

        layout.setStyle("-fx-padding: 20;");
    }


    /**
     * Returns the root node of this screen's UI layout.
     *
     * @return the JavaFX Parent node containing the screen layout
     */
    public Parent getView() {
        return layout;
    }
}
