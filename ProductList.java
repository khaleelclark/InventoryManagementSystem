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

    public ArrayList<Product> getUnderstockedProducts() {
        return null;
        //TODO
    }
}
