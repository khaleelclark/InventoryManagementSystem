/**
 * Khaleel Zindel Clark
 * CEN 3024 - Software Development 1
 * July 5, 2025
 * ProductList.java
 * This class creates a custom ProductList object
 * that extends ArrayList and has custom crud methods
 */
package src;

import java.util.ArrayList;

public class ProductList extends ArrayList<Product> {

    /**
     * method: getTotalInventoryEstimate
     * parameters: none
     * return: Double
     * purpose: this method calculates the estimated value of
     * items in the inventory.
     */
    public double getTotalInventoryEstimate() {
        if (this.isEmpty()) {
            return 0.0;
        }

        double total = 0;
        for (Product p : this) {
            total += p.getEstimatedCost() * p.getQuantity();
        }
        return total;
    }

    /**
     * method: getUnderstockedProducts
     * parameters: none
     * return: ProductList
     * purpose: this method returns all products in
     * the inventory that are below their expected stock
     */
    public ProductList getUnderstockedProducts() {
        ProductList understockedProducts = new ProductList();

        for (Product p : this) {
            if (p.getQuantity() < p.getExpectedQuantity()) {
                understockedProducts.add(p);
            }
        }
        return understockedProducts;
    }

}
