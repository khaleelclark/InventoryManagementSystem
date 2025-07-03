import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Stream;


/**
 * Khaleel Zindel Clark
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * ProductManager.java
 * This class creates houses the core logic for the IMS
 */
public class ProductManager {
    private static final ProductList products = new ProductList();
    public static Scanner scanner = new Scanner(System.in);


    /**
     * method: startIMS
     * parameters: none
     * return: void
     * purpose: runs the functions of the IMS based off the user's input
     */
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
                    boolean addProductFromFileSuccessful = addProductFromFile();
                    if (addProductFromFileSuccessful) {
                        System.out.println("Products added successfully!");
                    } else {
                        System.out.println("No Products were added.");
                    }
                    break;
                case "2":
                    boolean addProductManuallySuccessful = addProductManuallyPrompts();
                    if (addProductManuallySuccessful) {
                        System.out.println("Product added successfully!");
                    } else {
                        System.out.println("No Products were added.");
                    }
                    break;
                case "3":
                    boolean view = viewAllProducts();
                    if (!view) {
                        System.out.println("\nThere are no products in the list to view. Add some now!");
                    }
                    break;
                case "4":
                    removeProduct();
                    break;
                case "5":
                    boolean updateProduct = updateProduct();
                    if (updateProduct) {
                        System.out.println("Product updated successfully!");
                        viewAllProducts();
                    } else {
                        System.out.println("No product(s) have been updated.");
                    }
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
                        System.out.println("Product not updated.");
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


    /**
     * method: addProductFromFile
     * parameters: none
     * return: boolean
     * purpose: this method prompts the user for the path of the .txt or .csv file
     * they wish to use to load the IMS, and then it adds the Products to the Product list
     * using the addProduct method.
     */
    public static boolean addProductFromFile() {
        System.out.println("""
                Enter the absolute, or relative path of the file you wish to use to load the LMS, without quotations.
                Absolute Ex. C:\\InventorySystem\\Inventory\\Products\\products.csv
                Relative Ex. .\\Products\\products.csv""");

        String filePath = scanner.nextLine();
        File file = new File(filePath);

        String lowerCasePath = filePath.toLowerCase();
        if (!(lowerCasePath.endsWith(".csv") || lowerCasePath.endsWith(".txt"))) {
            System.err.println("Error: Only .csv and .txt files are allowed.");
            return false;
        }

        try {
            if (file.length() > 500_000) {
                System.err.println("Error: File too large. Please upload a smaller product file.");
                return false;
            }

            try (Stream<String> lines = Files.lines(file.toPath())) {
                long lineCount = lines.count();
                if (lineCount > 100) {
                    System.err.println("Error: File contains too many lines. Please limit your input to 100 products.");
                    return false;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file for validation: " + e.getMessage());
            return false;
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
                int nameCode = isValidProductName(productName);
                if (nameCode != ErrorCodes.OK) {
                    System.err.println("Error on line " + line + ": Invalid product name '" + productName + "'");
                    printValidationError(nameCode);
                    line++;
                    continue;
                }

                int quantity, expectedQuantity;
                double estimatedCost;
                Category category;
                String location = columns[5].trim();

                try {
                    quantity = Integer.parseInt(columns[1].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Error on line " + line + ": Quantity must be a number between 0 and 100.");
                    line++;
                    continue;
                }

                int quantityCode = isValidQuantity(quantity);
                if (quantityCode != ErrorCodes.OK) {
                    System.err.print("Error on line " + line + ": ");
                    printValidationError(quantityCode);
                    line++;
                    continue;
                }

                try {
                    expectedQuantity = Integer.parseInt(columns[2].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Error on line " + line + ": Expected quantity must be a number between 0 and 100.");
                    line++;
                    continue;
                }

                int expectedQuantityCode = isValidQuantity(expectedQuantity);
                if (expectedQuantityCode != ErrorCodes.OK) {
                    System.err.print("Error on line " + line + ": ");
                    printValidationError(expectedQuantityCode);
                    line++;
                    continue;
                }

                try {
                    estimatedCost = Double.parseDouble(columns[3].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Error on line " + line + ": Estimated cost must be a positive number.");
                    line++;
                    continue;
                }

                int costCode = isValidEstimatedCost(estimatedCost);
                if (costCode != ErrorCodes.OK) {
                    System.err.print("Error on line " + line + ": ");
                    printValidationError(costCode);
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

                int locationCode = isValidProductName(location);
                if (locationCode != ErrorCodes.OK) {
                    System.err.println("Error on line " + line + ": Invalid location '" + location + "'");
                    printValidationError(locationCode);
                    line++;
                    continue;
                }

                int statusCode = addProduct(productName, quantity, expectedQuantity, estimatedCost, category, location);
                if (statusCode != ErrorCodes.OK) {
                    System.err.print("Error on line " + line + ": ");
                    printValidationError(statusCode);
                    line++;
                }
            }

            if (!products.isEmpty()) {
                System.out.println("Loaded " + products.size() + " products from file.");
                System.out.println("\nFile loaded successfully!\nCurrent Products:");
                viewAllProducts();
                return true;
            } else {
                return false;
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error opening file: " + e.getMessage());
            return false;
        }
    }


    /**
     * method: addProductManuallyPrompts
     * parameters: none
     * return: boolean
     * purpose: this method handles the ui to allows the user to manually add a Product to the
     * product list by inputting a products attributes, and call the add product
     * method to add the product.
     */
    public static boolean addProductManuallyPrompts() {
        String name;
        while (true) {
            System.out.println("\nPlease enter the name of the product you wish to add or enter 'c' to cancel");
            name = scanner.nextLine();
            if (name.equalsIgnoreCase("c")) {
                return false;
            }
            int code = isValidProductName(name);
            if (code == ErrorCodes.OK) {
                break;
            } else {
                printValidationError(code);
            }

        }

        int quantity;
        while (true) {
            System.out.println("\nPlease enter the quantity you wish to add or enter 'c' to cancel");
            String quantityString = scanner.nextLine();
            if (quantityString.equalsIgnoreCase("c")) {
                return false;
            }

            try {
                quantity = Integer.parseInt(quantityString);
                int validationCode = isValidQuantity(quantity);
                if (validationCode == ErrorCodes.OK) {
                    break;
                } else {
                    printValidationError(validationCode);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: The quantity amount must be a number between 0 and 100");
            }
        }

        int expectedQuantity;
        while (true) {
            System.out.println("\nPlease enter the expected quantity of the item you wish to add or enter 'c' to cancel");
            String expectedQuantityString = scanner.nextLine();
            if (expectedQuantityString.equalsIgnoreCase("c")) {
                return false;
            }

            try {
                expectedQuantity = Integer.parseInt(expectedQuantityString);
                int validationCode = isValidQuantity(expectedQuantity);
                if (validationCode == ErrorCodes.OK) {
                    break;
                } else {
                    printValidationError(validationCode);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: The expected quantity amount must be a number between 0 and 100");
            }
        }

        double estimatedCost;
        while (true) {
            System.out.println("\nPlease enter the estimated cost of the item you wish to add or enter 'c' to cancel");
            String estimatedCostScan = scanner.nextLine();
            if (estimatedCostScan.equalsIgnoreCase("c")) {
                return false;
            }

            try {
                estimatedCost = Double.parseDouble(estimatedCostScan);
                int validationCode = isValidEstimatedCost(estimatedCost);
                if (validationCode == ErrorCodes.OK) {
                    break;
                } else {
                    printValidationError(validationCode);
                }
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
            if (location.equalsIgnoreCase("c")) {
                return false;
            }

            int code = isValidProductName(location);
            if (code == ErrorCodes.OK) {
                break;
            } else {
                printValidationError(code);
            }
        }

        while (true) {
            System.out.println("Do you want to save this product? (y/n)");
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("y")) {
                int statusCode = addProduct(name, quantity, expectedQuantity, estimatedCost, category, location);
                if (statusCode == ErrorCodes.OK) {
                    return true;
                } else {
                    printValidationError(statusCode);
                }
            } else if (confirm.equalsIgnoreCase("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
    }

    /**
     * method: addProduct
     * parameters: String name, int quantity, int expectedQuantity, double estimatedCost, Category category, String location
     * return: int
     * purpose: this method holds the business logic for adding a product.
     */
    public static int addProduct(String name, int quantity, int expectedQuantity, double estimatedCost, Category category, String location) {
        int nameCode = isValidProductName(name);
        if (nameCode != ErrorCodes.OK) return nameCode;

        int quantityCode = isValidQuantity(quantity);
        if (quantityCode != ErrorCodes.OK) return quantityCode;

        int costCode = isValidEstimatedCost(estimatedCost);
        if (costCode != ErrorCodes.OK) return costCode;

        Product p = new Product(name, quantity, expectedQuantity, estimatedCost, category, location);
        products.add(p);
        return ErrorCodes.OK;
    }

    /**
     * method: isValidProductName
     * parameters: String name
     * return: boolean
     * purpose: this method checks user input strings for product names and
     * locations to ensure they are in the correct format for the IMS
     */
    public static int isValidProductName(String name) {
        if (name == null || name.trim().isEmpty()) return ErrorCodes.NAME_EMPTY;
        if (name.length() < 2) return ErrorCodes.NAME_TOO_SHORT;
        if (name.length() > 50) return ErrorCodes.NAME_TOO_LONG;
        if (!name.matches("[a-zA-Z0-9 '\\-().]+")) return ErrorCodes.NAME_INVALID_CHARACTERS;
        return ErrorCodes.OK;
    }

    /**
     * method: isValidQuantity
     * parameters: int quantity
     * return: int
     * purpose: this method verifies user inputted quantity and estimated quantity
     * aligns with the business rules for the application.
     */
    public static int isValidQuantity(int quantity) {
        return (quantity >= 0 && quantity <= 100) ? ErrorCodes.OK : ErrorCodes.QUANTITY_OUT_OF_RANGE;
    }

    /**
     * method: isValidEstimatedCost
     * parameters: double cost
     * return: int
     * purpose: this method verifies user inputted cost aligns with the
     * business rules for the application.
     */
    public static int isValidEstimatedCost(double cost) {
        return cost >= 0 ? ErrorCodes.OK : ErrorCodes.COST_NEGATIVE;
    }


    /**
     * method: printValidationError
     * parameters: none
     * return: void
     * purpose: this method displays error messages based off
     * of corresponding codes defined in the ErrorCodes class.
     */
    public static void printValidationError(int code) {
        switch (code) {
            case ErrorCodes.NAME_EMPTY:
                System.err.println("Error: Product name cannot be empty.");
                break;
            case ErrorCodes.NAME_TOO_SHORT:
                System.err.println("Error: Product name must be at least 2 characters long.");
                break;
            case ErrorCodes.NAME_TOO_LONG:
                System.err.println("Error: Product name must be less than 50 characters.");
                break;
            case ErrorCodes.NAME_INVALID_CHARACTERS:
                System.err.println("Error: Product name contains invalid characters.");
                System.err.println("Allowed: letters, numbers, spaces, apostrophes ('), dashes (-), and parentheses.");
                break;
            case ErrorCodes.QUANTITY_OUT_OF_RANGE:
                System.err.println("Error: Quantity must be between 0 and 100.");
                break;
            case ErrorCodes.COST_NEGATIVE:
                System.err.println("Error: Estimated cost must be a positive number.");
                break;
            case ErrorCodes.NOT_FOUND:
                System.err.println("Error: Name not found.");
                break;
            case ErrorCodes.NOT_UPDATED:
                System.out.println("Product Not Updated.");
                break;
            default:
                System.err.println("Unknown error. Code: " + code);
        }
    }


    /**
     * method: viewAllProducts
     * parameters: none
     * return: void
     * purpose: this method displays all products in the inventory to the console
     */
    public static boolean viewAllProducts() {
        if (products.isEmpty()) {
            return false;
        } else {
            System.out.println("All products: ");
            for (Product p : products) {
                System.out.println(p.getProductInformation());

            }
        }
        return true;
    }

    /**
     * method: isValidProductName
     * parameters: none
     * return: void
     * purpose: this method displays a products name and quantity
     * for updating products
     */
    public static void viewAllProductsByNameAndQuantity() {
        if (products.isEmpty()) {
            System.out.println("\nThere are no products in the list to view. Add some now!");
        } else {
            for (Product p : products) {
                System.out.println(p.getQuantityInformation());

            }
        }
    }

    /**
     * method: removeProduct
     * parameters: none
     * return: void
     * purpose: this method holds the ui logic for removing a product
     * then calls the remove product by name method for removal.
     */
    public static void removeProduct() {
        if (products.isEmpty()) {
            return;
        }

        System.out.println("Please enter the name of the product you wish to remove:");
        viewAllProducts();
        String name = scanner.nextLine().trim();

        int result = removeProductByName(name, products);

        switch (result) {
            case ErrorCodes.OK -> {
                System.out.println("Product '" + name + "' removed successfully.");
            }
            case ErrorCodes.NOT_FOUND -> {
                System.err.println("There are no products with the name: " + name + ". Please try again.");
            }
            case ErrorCodes.NO_PRODUCTS -> {
                System.err.println("No products available to remove.");
            }
            default -> {
                System.err.println("Unknown error occurred.");
            }
        }

    }


    /**
     * method: removeProductByName
     * parameters: String name
     * return: int
     * purpose: this method holds the business logic for removing products
     */
    public static int removeProductByName(String name, ProductList productList) {
        if (productList.isEmpty()) {
            return ErrorCodes.NO_PRODUCTS;
        }
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getName().equalsIgnoreCase(name)) {
                productList.remove(i);
                return ErrorCodes.OK;
            }
        }
        return ErrorCodes.NOT_FOUND;
    }


    /**
     * method: updateProduct
     * parameters: none
     * return: boolean
     * purpose: this method displays all products then prompts the user to input the
     * fields they would like to update, allowing the user to enter past fields they do not want to change.
     */
    public static boolean updateProduct() {

        if (products.isEmpty()) {
            System.out.println("\nThere are no products to update. Add some now!");
            return false;
        }

        Product product = null;

        while (product == null) {
            System.out.print("Enter the name of the product to update, or enter 'c' to cancel\n");
            viewAllProducts();
            String inputName = scanner.nextLine().trim();

            if (inputName.equalsIgnoreCase("c")) {
                return false;
            }

            if (!containsProductWithName(inputName, products)) {
                System.err.println("No product found with the name: " + inputName);
            } else {
                product = getProductByName(products, inputName);
            }
        }


        System.out.println("\nLeave a field blank to keep the current value.");

        while (true) {
            System.out.print("New name (" + product.getName() + "): ");
            String newName = scanner.nextLine().trim();

            if (newName.isBlank()) {
                break;
            }
            int code = isValidProductName(newName);
            if (code == ErrorCodes.OK) {
                product.setName(newName);
                break;
            } else {
                printValidationError(code);
            }
        }

        while (true) {
            System.out.print("New quantity (" + product.getQuantity() + "): ");
            String quantityInput = scanner.nextLine().trim();

            if (quantityInput.isBlank()) {
                break;
            }

            try {
                int newQty = Integer.parseInt(quantityInput);
                int code = isValidQuantity(newQty);
                if (code == ErrorCodes.OK) {
                    product.setQuantity(newQty);
                    break;
                } else {
                    printValidationError(code);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid number: " + quantityInput + " Please try again with a valid number.");
            }
        }

        while (true) {
            System.out.print("New expected quantity (" + product.getExpectedQuantity() + "): ");
            String expectedQuantityInput = scanner.nextLine().trim();

            if (expectedQuantityInput.isBlank()) {
                break;
            }

            try {
                int newExpected = Integer.parseInt(expectedQuantityInput);
                int code = isValidQuantity(newExpected);
                if (code == ErrorCodes.OK) {
                    product.setExpectedQuantity(newExpected);
                    break;
                } else {
                    printValidationError(code);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid number: " + expectedQuantityInput + " Please try again with a valid number.");
            }
        }
        while (true) {
            System.out.print("New estimated cost (" + product.getEstimatedCost() + "): ");
            String estimatedCostInput = scanner.nextLine().trim();

            if (estimatedCostInput.isBlank()) {
                break;
            }
            try {
                double newCost = Double.parseDouble(estimatedCostInput);
                int code = isValidEstimatedCost(newCost);
                if (code == ErrorCodes.OK) {
                    product.setEstimatedCost(newCost);
                    break;
                } else {
                    printValidationError(code);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid number: " + estimatedCostInput + " Please try again with a valid number.");
            }
        }

        while (true) {
            System.out.println("\nSelect a new category by number, or leave blank to keep current (" + product.getCategory().getCategoryName() + "):");

            Category[] categories = Category.values();
            for (int i = 0; i < categories.length; i++) {
                System.out.println((i + 1) + ". " + categories[i].getCategoryName());
            }
            String input = scanner.nextLine().trim();

            if (input.isBlank()) {
                break;
            }
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= categories.length) {
                    Category selectedCategory = categories[choice - 1];
                    System.out.println("You selected: " + selectedCategory.getCategoryName());
                    product.setCategory(selectedCategory);
                    break;
                } else {
                    System.err.println("Invalid number. Please choose between 1 and " + categories.length + ".");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a number.");
            }
        }

        while (true) {
            System.out.print("New location (" + product.getLocation() + "): ");
            String newLocation = scanner.nextLine().trim();

            if (newLocation.isBlank()) {
                break;
            }
            int code = isValidProductName(newLocation);
            if (code == ErrorCodes.OK) {
                product.setLocation(newLocation);
                break;
            } else {
                printValidationError(code);
            }
        }
        return true;
    }


    /**
     * method: getProductByName
     * parameters: String inputName
     * return: Product
     * purpose: this method returns the product with the name that matches a user's input
     */
    public static Product getProductByName(ProductList productList, String inputName) {
        return productList.stream()
                .filter(p -> p.getName().equalsIgnoreCase(inputName))
                .findFirst()
                .orElse(null);
    }

    /**
     * method: updateProductQuantity
     * parameters: String productName, int newQuantity
     * return: boolean
     * purpose: this method holds the ui logic to prompt the user for a product name
     * to update its quantity.
     */
    public static boolean updateProductQuantity(String productName, int newQuantity) {
        if (!containsProductWithName(productName, products)) {
            System.err.println("There are no products with the name: " + productName + ". Please try again.");
            return false;
        }
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(productName)) {
                System.out.println("Do you want to update this product? (y/n)");
                String confirm = scanner.nextLine();

                if (confirm.equalsIgnoreCase("y")) {
                    int result = updateProductQuantityDirectly(productName, newQuantity, products);
                    if (result == ErrorCodes.OK) {
                        return true;
                    } else {
                        printValidationError(result);
                    }
                } else if (confirm.equalsIgnoreCase("n")) {
                    return false;
                } else {
                    System.out.println("Invalid input. Please enter 'y' or 'n'.");
                }
            }
        }
        return false;

    }

    /**
     * method: updateProductQuantityDirectly
     * parameters: String productName, int newQuantity
     * return: int
     * purpose: this method holds the business logic for updating product quantities.
     */
    public static int updateProductQuantityDirectly(String productName, int newQuantity, ProductList productList) {
        if (!containsProductWithName(productName, productList)) {
            return ErrorCodes.NOT_FOUND;
        }
        for (Product p : productList) {
            if (p.getName().equalsIgnoreCase(productName)) {
                int code = isValidQuantity(newQuantity);
                if (code != ErrorCodes.OK) return code;
                p.setQuantity(newQuantity);
                return ErrorCodes.OK;
            }
        }
        return ErrorCodes.NOT_UPDATED;
    }

    /**
     * method: containsProductWithName
     * parameters: String inputName
     * return: boolean
     * purpose: this method verifies that a product with the name the user
     * inputs exists
     */
    public static boolean containsProductWithName(String inputName, ProductList products) {
        return products.stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(inputName));
    }

    public static ProductList getProducts() {
        return products;
    }

}
