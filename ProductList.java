import java.util.ArrayList;

public class ProductList extends ArrayList<Product> {
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
