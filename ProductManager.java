import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Stream;

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
            System.out.println("8. View all understocked products");
            System.out.println("9. Exit the Inventory Management System");

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
                    if (!products.isEmpty()) {
                        System.out.println("Please enter the name of the product you wish to remove:");
                        viewAllProducts();
                        String name = scanner.nextLine();
                        removeProduct(name);
                    } else {
                        System.out.println("\nThere are no products in the list to remove. Add some now!");
                    }
                    break;
                case "5":
                    updateProduct();
                    break;
                case "6": {
                    if (products.isEmpty()) {
                        System.out.println("\nThere are no products in the list to edit. Add some now!");
                        break;
                    }
                    viewAllProductsByNameAndQuantity();
                    System.out.println("Please enter the name of the product you'd like to update:");
                    String name = scanner.nextLine();

                    System.out.println("Please enter the updated " + name + " quantity:");
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
                    double total = products.getTotalInventoryEstimate();
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
                    System.out.println("Total estimated inventory value: " + currencyFormat.format(total));
                    break;
                }
                case "8": {
                    products.getUnderstockedProducts();
                    break;
                }
                case "9": {
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
                System.out.println("\nPlease enter the name of the product you wish to add or enter 'c' to cancel");
                name = scanner.nextLine();
                if (name.equals("c")) {
                    return;
                }
                if (isValidProductName(name)) {
                    break;
                } else {
                    System.out.println("Please try again");
                }
            }

            int quantity;
            while (true) {
                System.out.println("\nPlease enter the quantity you wish to add or enter 'c' to cancel");
                String quantityString = scanner.nextLine();
                if (quantityString.equals("c")) {
                    return;
                }

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
                System.out.println("\nPlease enter the expected quantity of the item you wish to add or enter 'c' to cancel");
                String expectedQuantityString = scanner.nextLine();
                if (expectedQuantityString.equals("c")) {
                    return;
                }

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
                System.out.println("\nPlease enter the estimated cost of the item you wish to add or enter 'c' to cancel");
                String estimatedCostScan = scanner.nextLine();
                if (estimatedCostScan.equals("c")) {
                    return;
                }

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
                System.out.println("\nPlease enter the location of the product you wish to add, or enter 'c' to cancel");
                location = scanner.nextLine();
                if (location.equals("c")) {
                    return;
                }
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
        System.out.println("""
                Enter the absolute, or relative path of the file you wish to use to load the LMS, without quotations.
                Absolute Ex. C:\\InventorySystem\\Inventory\\Products\\products.csv
                Relative Ex. .\\Products\\products.csv""");

        String filePath = scanner.nextLine();
        File file = new File(filePath);

        String lowerCasePath = filePath.toLowerCase();
        if (!(lowerCasePath.endsWith(".csv") || lowerCasePath.endsWith(".txt"))) {
            System.err.println("Error: Only .csv and .txt files are allowed.");
            return;
        }

        try {
            if (file.length() > 500_000) {
                System.err.println("Error: File too large. Please upload a smaller product file.");
                return;
            }

            try (Stream<String> lines = Files.lines(file.toPath())) {
                long lineCount = lines.count();
                if (lineCount > 100) {
                    System.err.println("Error: File contains too many lines. Please limit your input to 100 products.");
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file for validation: " + e.getMessage());
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            int line = 1;

            while (fileScanner.hasNextLine()) {
                String input = fileScanner.nextLine();
                String[] columns = input.split(",");

                if (columns.length != 6) {
                    System.err.println("Error on line " + line + ": Expected 6 columns but found " + columns.length);
                    line++;
                    continue;
                }

                String productName = columns[0].trim();
                if (!isValidProductName(productName)) {
                    System.err.println("Error on line " + line + ": Invalid product name '" + productName + "'");
                    line++;
                    continue;
                }

                int quantity, expectedQuantity;
                double estimatedCost;
                Category category;
                String location = columns[5].trim();

                try {
                    quantity = Integer.parseInt(columns[1].trim());
                    if (quantity <= 0 || quantity > 100) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    System.err.println("Error on line " + line + ": Quantity must be a number between 1 and 100.");
                    line++;
                    continue;
                }

                try {
                    expectedQuantity = Integer.parseInt(columns[2].trim());
                    if (expectedQuantity <= 0 || expectedQuantity > 100) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    System.err.println("Error on line " + line + ": Expected quantity must be a number between 1 and 100.");
                    line++;
                    continue;
                }

                try {
                    estimatedCost = Double.parseDouble(columns[3].trim());
                    if (estimatedCost < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    System.err.println("Error on line " + line + ": Estimated cost must be a positive number.");
                    line++;
                    continue;
                }

                try {
                    category = Category.valueOf(columns[4].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.err.println("Error on line " + line + ": Invalid category '" + columns[4].trim() + "'");
                    line++;
                    continue;
                }

                if (!isValidProductName(location)) {
                    System.err.println("Error on line " + line + ": Invalid location '" + location + "'");
                    line++;
                    continue;
                }

                products.add(new Product(productName, quantity, expectedQuantity, estimatedCost, category, location));
                line++;
            }

            if (!products.isEmpty()) {
                System.out.println("\nFile loaded successfully!\nCurrent Products:");
                viewAllProducts();
            } else {
                System.out.println("No Products were added.");
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error opening file: " + e.getMessage());
        }
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

        if (!name.matches("[a-zA-Z0-9 '\\-()]+")) {
            System.out.println("Error: Product name contains invalid characters.");
            System.out.println("Allowed: letters, numbers, spaces, apostrophes ('), dashes (-), and parentheses");
            return false;
        }

        return true;
    }

    public static void viewAllProducts() {
        if (products.isEmpty()) {
            System.out.println("\nThere are no products in the list to view. Add some now!");
        } else {
            for (Product p : products) {
                System.out.println(p.getProductInformation());

            }
        }
    }

    public static void viewAllProductsByNameAndQuantity() {
        if (products.isEmpty()) {
            System.out.println("\nThere are no products in the list to view. Add some now!");
        } else {
            for (Product p : products) {
                System.out.println(p.getQuantityInformation());

            }
        }
    }

    public static void removeProduct(String name) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equalsIgnoreCase(name)) {
                products.remove(i);
                return;
            }
        }

        System.err.println("There are no products with the name: " + name + ". Please try again.");
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
        if (!containsProductWithName(productName)) {
            System.err.println("There are no products with the name: " + productName + ". Please try again.");
            return false;
        }
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(productName)) {
                System.out.println("Do you want to update this product? (y/n)");
                String confirm = scanner.nextLine();

                if (confirm.equalsIgnoreCase("y")) {
                    p.setQuantity(newQuantity);
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    public static boolean containsProductWithName(String inputName) {
        //done
        return products.stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(inputName));
    }
}
