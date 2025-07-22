package src.screens;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import src.Product;
import src.ProductList;
import src.ProductManager;
import src.ScreenController;

/**
 * This class creates the Remove Product screen and handles
 * all the UI logic for removing products from the inventory.
 * <p>
 * It displays a table of products with their details and allows
 * the user to select and remove a product with confirmation.
 * </p>
 *
 * @author Khaleel Zindel Clark
 * @version July 5, 2025
 */
public class RemoveProductScreen {
    private final VBox layout;

    /**
     * Constructs the Remove Product screen UI.
     * <p>
     * Displays a table listing all products, a button to remove
     * the selected product, and a back button to return to the home screen.
     * If no products exist, it shows a message and disables removal.
     * </p>
     *
     * @param controller the ScreenController for screen navigation
     */
    public RemoveProductScreen(ScreenController controller) {

        Label title = new Label("Remove Product");

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

        ProductList productList = ProductManager.getProducts();
        productTable.getItems().setAll(productList);

        Button removeButton = getButton(productTable);

        if (productList.isEmpty()) {
            title.setText("No products found to remove. Add products now!");
            productTable.setVisible(false);
            productTable.setManaged(false);
            removeButton.setVisible(false);
            removeButton.setManaged(false);
        }

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(_ -> controller.activate("home"));

        layout = new VBox(10, title, productTable, removeButton, backButton);
        layout.setStyle("-fx-padding: 20;");
    }

    /**
     * Creates and returns the Remove Product button with its event handler.
     * <p>
     * When clicked, the button checks if a product is selected,
     * prompts the user for confirmation, and removes the product if confirmed.
     * Alerts are shown for no selection, confirmation, and errors where applicable.
     * </p>
     *
     * @param productTable the TableView displaying the list of products
     * @return a configured Button that handles product removal
     */
    private static Button getButton(TableView<Product> productTable) {
        Button removeButton = new Button("Remove Product");
        removeButton.setOnAction(_ -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Selection");
                alert.setHeaderText(null);
                alert.setContentText("Please select a product to remove.");
                alert.showAndWait();
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Removal");
            confirmation.setHeaderText("Are you sure you want to remove this product?");
            confirmation.setContentText("Product: " + selected.getName());

            confirmation.showAndWait().ifPresent(response -> {
                if (response.getButtonData().isDefaultButton()) {
                    int result = ProductManager.removeProduct(selected.getName(), ProductManager.getProducts());
                    if (result == 0) {
                        productTable.getItems().setAll(ProductManager.getProducts());
                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setHeaderText("Could not remove product");
                        error.setContentText("Error code: " + result);
                        error.showAndWait();
                    }
                }
            });

        });
        return removeButton;
    }

    /**
     * Returns the root layout node containing all UI components of this screen.
     *
     * @return the VBox layout of the Remove Product screen
     */
    public VBox getView() {
        return layout;
    }
}