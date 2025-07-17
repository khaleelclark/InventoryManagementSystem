package src;

/**
 * Represents a product in the inventory with attributes such as
 * name, quantity, expected quantity, estimated cost, category, and location.
 *
 * <p>This class provides getters and setters for all attributes,
 * along with methods to retrieve formatted product information.</p>
 *
 * @author Khaleel Zindel Clark
 * @version July 18, 2025
 */
public class Product {
    private String name;
    private int quantity;
    private int expectedQuantity;
    private double estimatedCost;
    private Category category;
    private String location;


    /**
     * Constructs a new {@code Product} with the specified attributes.
     *
     * @param name            the product name
     * @param quantity        the current quantity
     * @param expectedQuantity the expected quantity to maintain
     * @param estimatedCost   the estimated cost of the product
     * @param category        the product category
     * @param location        the storage location of the product
     */
    public Product(String name, int quantity, int expectedQuantity, double estimatedCost, Category category, String location) {
        this.name = name;
        this.quantity = quantity;
        this.expectedQuantity = expectedQuantity;
        this.estimatedCost = estimatedCost;
        this.category = category;
        this.location = location;
    }

    /**
     * Returns the product name.
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product name.
     * @param name the product name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the product quantity.
     * @return the product quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the product quantity.
     * @param quantity the product quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the product expected quantity.
     * @return the product expected quantity
     */
    public int getExpectedQuantity() {
        return expectedQuantity;
    }

    /**
     * Returns the product estimated cost.
     * @return the product estimated cost
     */
    public double getEstimatedCost() {
        return estimatedCost;
    }

    /**
     * Returns the product category.
     * @return the product category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the product category.
     * @param category the product category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Returns the product location.
     * @return the product location
     */
    public String getLocation() {
        return location;
    }
}
