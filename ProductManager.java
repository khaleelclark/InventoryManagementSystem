import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class ProductManager {
    private static final ProductList products = new ProductList();
    public static Scanner scanner = new Scanner(System.in);

    public static void startIMS() {
        while (true) {
            System.out.println("\nWelcome to Zindel's Inventory Management System!\nPlease enter the number of the option you wish to select\n");
            System.out.println("1. Add a Product from a File");
            System.out.println("2. Add a Product Manually");
            System.out.println("3. Display all Products");
            System.out.println("4. Remove a Product");
            System.out.println("5. Update a Product");
            System.out.println("6. Update a Product's Quantity Directly");
            System.out.println("7. Calculate the Estimated Total Value of the Inventory Management System");
            System.out.println("8. Exit the Inventory Management System");

            switch (scanner.nextLine()) {
                case "1":
                    addProductFromFile();
                    break;
                case "2":
                    addProductManually();
                    break;
                case "3":
                    viewAllProducts();
                    break;
                case "4":
                    removeProduct();
                    break;
                case "5":
                    updateProduct();
                    break;
                case "6": {
                    if (products.isEmpty()) {
                        System.out.println("\nThere are no products in the list to edit. Add some now!");
                        break;
                    }
                    System.out.println("Please enter the name of the product you'd like to update:");
                    String name = scanner.nextLine();

                    System.out.println("Please enter the updated quantity:");
                    int quantity = Integer.parseInt(scanner.nextLine());

                    boolean updated = updateProductQuantity(name, quantity);

                    if (updated) {
                        System.out.println("Product quantity updated.");
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;
                }
                case "7": {
                    double total = calculateEstimatedTotalInventoryValue();
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
                    System.out.println("Total estimated inventory value: " + currencyFormat.format(total));
                    break;
                }
                case "8": {
                    System.out.println("Thank you for using Zindel's IMS");
                    System.exit(0);
                    break;
                }
                default:
                    System.err.println("Error: invalid entry.\n");
                    break;
            }
        }
    }


    public static void addProductManually() {
        while (true) {
            String name;
            while (true) {
                System.out.println("\nPlease enter the name of the product you wish to add:");
                name = scanner.nextLine();
                if (isValidProductName(name)) {
                    break;
                } else {
                    System.out.println("Please try again");
                }
            }

            int quantity;
            while (true) {
                System.out.println("\nPlease enter the quantity you wish to add:");
                String quantityString = scanner.nextLine();

                try {
                    quantity = Integer.parseInt(quantityString);
                    if (quantity <= 0 || quantity > 100) {
                        throw new NumberFormatException("Quantity should be between 0 and 100");
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("Error: The quantity amount must be a number between 0 and 100");
                }
            }

            int expectedQuantity;
            while (true) {
                System.out.println("\nPlease enter the expected quantity of the item you wish to add:");
                String expectedQuantityString = scanner.nextLine();

                try {
                    expectedQuantity = Integer.parseInt(expectedQuantityString);
                    if (expectedQuantity <= 0 || expectedQuantity > 100) {
                        throw new NumberFormatException("Quantity should be between 0 and 100");
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("Error: The expected quantity amount must be a number between 0 and 100");
                }
            }

            double estimatedCost;
            while (true) {
                System.out.println("\nPlease enter the estimated cost of the item you wish to add:");
                String estimatedCostScan = scanner.nextLine();
                try {
                    estimatedCost = Double.parseDouble(estimatedCostScan);
                    if (estimatedCost < 0) {
                        throw new NumberFormatException("The expected cost amount must be a positive number");
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("Error: The expected cost amount must be a positive number");
                }
            }

            Category category;
            while (true) {
                System.out.println("\nPlease select a category by number:");

                Category[] categories = Category.values();
                for (int i = 0; i < categories.length; i++) {
                    System.out.println((i + 1) + ". " + categories[i].getCategoryName());
                }

                String input = scanner.nextLine();

                try {
                    int choice = Integer.parseInt(input);

                    if (choice >= 1 && choice <= categories.length) {
                        Category selectedCategory = categories[choice - 1];
                        System.out.println("You selected: " + selectedCategory.getCategoryName());
                        category = selectedCategory;
                        break;
                    } else {
                        System.err.println("Invalid number. Please choose between 1 and " + categories.length + ".");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid input. Please enter a number.");
                }
            }

            String location;
            while (true) {
                System.out.println("\nPlease enter the location of the product you wish to add:");
                location = scanner.nextLine();
                if (isValidProductName(location)) {
                    break;
                } else {
                    System.out.println("Please try again");
                }
            }

            System.out.println("Do you want to save this product? (y/n)");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("y")) {
                Product p = new Product(name, quantity, expectedQuantity, estimatedCost, category, location);
                products.add(p);
                System.out.println("Product added successfully.");
            } else {
                System.out.println("Product was not added.");
            }

            System.out.println("Would you like to add another product? (y/n)");
            String again = scanner.nextLine();
            if (!again.equalsIgnoreCase("y")) {
                break;
            }

        }
    }

    public static void addProductFromFile() {
        //TODO
    }

    public static boolean isValidProductName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Product name cannot be empty.");
            return false;
        }

        if (name.length() < 2) {
            System.out.println("Error: Product name must be at least 2 characters long.");
            return false;
        }

        if (name.length() > 50) {
            System.out.println("Error: Product name must be less than 50 characters.");
            return false;
        }

        if (!name.matches("[a-zA-Z0-9 '\\-(),]+")) {
            System.out.println("Error: Product name contains invalid characters.");
            System.out.println("Allowed: letters, numbers, spaces, apostrophes ('), dashes (-), parentheses, and commas.");
            return false;
        }

        return true;
    }

    public static void viewAllProducts() {
        //TODO
    }

    public static void removeProduct() {
        //TODO
    }

    public static void updateProduct() {
        //TODO
        String name;
        int quantity;
        int expectedQuantity;
        double estimatedCost;
        Category category;
        String location;
    }

    public static boolean updateProductQuantity(String productName, int newQuantity) {
        //done* needs test
        if (!containsProductWithName(productName)) {
            System.err.println("There are no products with the name: " + productName + ". Please try again.");
            return false;
        }
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(productName)) {
                p.setQuantity(newQuantity);
                return true;
            }
        }
        return false;
    }

    public static boolean containsProductWithName(String inputName) {
        //done
        return products.stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(inputName));
    }

    public static double calculateEstimatedTotalInventoryValue() {
        //done
        if (products.isEmpty()) {
            System.out.println("\nYour estimated value is: $0.00. There are no products in your inventory. Add some products now to calculate your estimated value!");
            return 0.0;
        }
        double total = 0;
        for (Product p : products) {
            total += p.getEstimatedCost() * p.getQuantity();
        }
        return total;
    }


}
