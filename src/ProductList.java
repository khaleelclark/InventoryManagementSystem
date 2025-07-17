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

/**
 * A custom list of {@link Product} objects that extends {@link ArrayList}
 * with additional inventory-specific methods for CRUD operations and analytics.
 *
 * <p>This class provides functionality beyond a standard list, including
 * calculating total inventory value and filtering understocked products.</p>
 *
 * @author Khaleel Zindel Clark
 * @version July 18, 2025
 */
public class ProductList extends ArrayList<Product> {

    /**
     * Calculates the total estimated value of all products in the inventory.
     *
     * <p>This method multiplies each product's estimated cost by its quantity
     * and sums these values to return the total inventory estimate.</p>
     *
     * @return the total estimated value of the inventory; {@code 0.0} if the list is empty
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
     * Returns a list of all products that are currently understocked.
     *
     * <p>A product is considered understocked if its current quantity is
     * less than its expected quantity.</p>
     *
     * @return a {@code ProductList} containing all understocked products;
     *         empty if none are understocked
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
