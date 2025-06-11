public class Product {
    private String name;
    private int quantity;
    private int expectedQuantity;
    private Category category;
    private String location;

    public Product(String name, int quantity, int expectedQuantity, Category category, String location) {
        this.name = name;
        this.quantity = quantity;
        this.expectedQuantity = expectedQuantity;
        this.category = category;
        this.location = location;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getExpectedQuantity() {
        return expectedQuantity;
    }
    public void setExpectedQuantity(int expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
}
