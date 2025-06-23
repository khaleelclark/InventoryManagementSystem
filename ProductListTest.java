import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductListTest {

    ProductList productList;

    @BeforeEach
    void setUp() {
        productList = new ProductList();
        productList.add(new Product("Milk", 2,3,2.99,Category.FOOD_BEVERAGES,"Fridge"));
        productList.add(new Product("Flour", 1,2,3.99,Category.FOOD_BEVERAGES,"Pantry"));
        productList.add(new Product("Bread", 1,1,1.99,Category.FOOD_BEVERAGES,"Pantry"));
    }

    @Test
    @DisplayName("Get Total Inventory Estimate")
    void getTotalInventoryEstimate() {
        double total = productList.getTotalInventoryEstimate();
        assertEquals(11.96, total);
    }

    @Test
    @DisplayName("Get Understocked Products")
    void getUnderstockedProducts() {
    }
}