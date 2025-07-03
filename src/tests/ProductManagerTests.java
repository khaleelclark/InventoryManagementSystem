package src.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import src.Category;
import src.Product;
import src.ProductList;
import src.ProductManager;

import static org.junit.jupiter.api.Assertions.*;

class ProductManagerTests {

    ProductList productList;
    Product p;
    Product m;

    @BeforeEach
    void setUp() {
        productList = new ProductList();
        p = new Product("Bread", 2, 2, 1.09, Category.FOOD_BEVERAGES, "Pantry");
        m = new Product("Eggs", 2, 4, 3.05, Category.FOOD_BEVERAGES, "Fridge");
        productList.add(p);
        productList.add(m);
    }

    @Test
    @DisplayName("Add Product")
    void addProduct() {
        int successCode = ProductManager.addProduct("Milk", 1, 2, 2.09, Category.FOOD_BEVERAGES, "Fridge");
        assertEquals(0, successCode);
    }

    @Test
    @DisplayName("Is valid product name")
    void isValidProductName() {
        String ValidName = "Milk";
        int successCode = ProductManager.isValidProductName(ValidName);
        assertEquals(0, successCode);

        String invalidName = "!@#$%^&cheap!";
        int failureCode = ProductManager.isValidProductName(invalidName);
        assertEquals(4, failureCode);
    }

    @Test
    @DisplayName("Is valid quantity")
    void isValidQuantity() {
        int validQuantity = 5;
        int successCode = ProductManager.isValidQuantity(validQuantity);
        assertEquals(0, successCode);

        int invalidQuantity = 101;
        int failureCode = ProductManager.isValidQuantity(invalidQuantity);
        assertEquals(5, failureCode);
    }

    @Test
    @DisplayName("Is valid estimated cost")
    void isValidEstimatedCost() {
        double validEstimatedCost = 5.00;
        int successCode = ProductManager.isValidEstimatedCost(validEstimatedCost);
        assertEquals(0, successCode);

        double invalidEstimatedCost = -1.0;
        int failureCode = ProductManager.isValidEstimatedCost(invalidEstimatedCost);
        assertEquals(6, failureCode);
    }

    @Test
    @DisplayName("Remove product by name")
    void removeProductByName() {
        int successCode = ProductManager.removeProductByName("Bread", productList);
        assertEquals(0, successCode);

        int failureCode = ProductManager.removeProductByName("Television", productList);
        assertEquals(8, failureCode);
    }

    @Test
    @DisplayName("Get product by name")
    void getProductByName() {
        Product success = ProductManager.getProductByName(productList, "eggs");
        assertEquals(m, success);

        Product failure = ProductManager.getProductByName(productList, "Charger");
        assertNull(failure);
    }

    @Test
    @DisplayName("Update quantity directly")
    void updateProductQuantityDirectly() {
        int success = ProductManager.updateProductQuantityDirectly("Eggs", 3, productList);
        assertEquals(0, success);

        int failure = ProductManager.updateProductQuantityDirectly("Laptop", 3, productList);
        assertEquals(8, failure);
    }

    @Test
    @DisplayName("Contains product with name")
    void containsProductWithName() {
        String name = "eggs";
        boolean success = ProductManager.containsProductWithName(name, productList);
        assertTrue(success);

        String badName = "Chocolate";
        boolean failure = ProductManager.containsProductWithName(badName, productList);
        assertFalse(failure);
    }
}