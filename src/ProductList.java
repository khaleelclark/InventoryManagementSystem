/**
 * Khaleel Zindel Clark
 * CEN 3024 - Software Development 1
 * June 18, 2025
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
            System.out.println("\nYour estimated value is: $0.00. There are no products in your inventory. Add some products now to calculate your estimated value!");
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
     * return: void
     * purpose: this method returns all products in
     * the inventory that are below their expected stock
     */
    public void getUnderstockedProducts() {
        ProductList understockedProducts = new ProductList();

        if (this.isEmpty()) {
            System.out.println("No products in inventory.");
            return;
        }

        for (Product p : this) {
            if (p.getQuantity() < p.getExpectedQuantity()) {
                understockedProducts.add(p);
            }
        }

        if (understockedProducts.isEmpty()) {
            System.out.println("No understocked products!");
        } else {
            System.out.println("Understocked Products:");
            for (Product p : understockedProducts) {
                System.out.println(p.getQuantityInformation());
            }
        }
    }

}
