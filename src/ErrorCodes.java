package src;

/**
 * Defines application-wide error code constants used to indicate
 * specific validation or operation results.
 *
 * <p>These codes are returned by methods to signal success or various
 * error conditions related to product validation, inventory operations,
 * and database updates.</p>
 *
 * Usage of these codes enables consistent error handling and messaging
 * across the Inventory Management System.
 *
 * @author Khaleel Zindel Clark
 * @version July 18, 2025
 */

public class ErrorCodes {
    // Success
    public static final int OK = 0;

    // Product name errors
    public static final int NAME_EMPTY = 1;
    public static final int NAME_TOO_SHORT = 2;
    public static final int NAME_TOO_LONG = 3;
    public static final int NAME_INVALID_CHARACTERS = 4;

    // Quantity errors
    public static final int QUANTITY_OUT_OF_RANGE = 5;

    // Estimated cost errors
    public static final int COST_NEGATIVE = 6;

    //Removal Errors
    public static final int NO_PRODUCTS = 7;
    public static final int NOT_FOUND = 8;
    public static final int NOT_UPDATED = 9;

}
