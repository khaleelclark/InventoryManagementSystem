package src;

/**
 * Khaleel Zindel Clark
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * Product.java
 * This class creates a product object with the attributes
 * name, quantity, expected quantity, estimated cost, category, and location
 */

public class Product {
    private String name;
    private int quantity;
    private int expectedQuantity;
    private double estimatedCost;
    private Category category;
    private String location;

    public Product(String name, int quantity, int expectedQuantity, double estimatedCost, Category category, String location) {
        this.name = name;
        this.quantity = quantity;
        this.expectedQuantity = expectedQuantity;
        this.estimatedCost = estimatedCost;
        this.category = category;
        this.location = location;
    }

    /**
     * method: getName
     * parameters: none
     * return: String
     * purpose: this method is a getter to retrieve the name of a product
     */
    public String getName() {
        return name;
    }

    /**
     * method: setName
     * parameters: String name
     * return: void
     * purpose: this method is a setter to set the name of a product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * method: getQuantity
     * parameters: none
     * return: int
     * purpose: this method is a getter to retrieve the quantity of a product
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * method: setQuantity
     * parameters: int Quantity
     * return: void
     * purpose: this method is a setter to set the quantity of a product
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * method: getExpectedQuantity
     * parameters: none
     * return: int
     * purpose: this method is a getter to retrieve the expected quantity of a product
     */
    public int getExpectedQuantity() {
        return expectedQuantity;
    }

    /**
     * method: setExpectedQuantity
     * parameters: int expectedQuantity
     * return: void
     * purpose: this method is a setter to set the expected quantity of a product
     */
    public void setExpectedQuantity(int expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }

    /**
     * method: getEstimatedCost
     * parameters: none
     * return: double
     * purpose: this method is a getter to retrieve the estimated cost of a product
     */
    public double getEstimatedCost() {
        return estimatedCost;
    }

    /**
     * method: setEstimatedCost
     * parameters: double estimatedCost
     * return: void
     * purpose: this method is a setter to set the estimated cost of a product
     */
    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    /**
     * method: getCategory
     * parameters: none
     * return: Category
     * purpose: this method is a getter to retrieve the Category of a product
     */
    public Category getCategory() {
        return category;
    }

    /**
     * method: setLocation
     * parameters: Category category
     * return: void
     * purpose: this method is a setter to set the category of a product
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * method: getLocation
     * parameters: none
     * return: String
     * purpose: this method is a getter to retrieve the location of a product
     */
    public String getLocation() {
        return location;
    }

    /**
     * method: setLocation
     * parameters: String location
     * return: void
     * purpose: this method is a setter to set the location of a product
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * method: getPatronInformation
     * parameters: none
     * return: String
     * purpose: this method returns patron attribute information in a formatted string
     */
    public String getProductInformation() {
        return "Name: " + name + " Quantity: " + quantity + " Expected Quantity: " + expectedQuantity + " Estimated Cost: " + estimatedCost + " Category: " + category.getCategoryName() + " Location: " + location;
    }

    /**
     * method: getPatronInformation
     * parameters: none
     * return: String
     * purpose: this method returns patron attribute information in a formatted string
     */
    public String getQuantityInformation() {
        return "Name: " + name + " Quantity: " + quantity + " Expected Quantity: " + expectedQuantity;
    }
}
