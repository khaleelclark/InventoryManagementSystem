package src;

/**
 * Khaleel Zindel Clark
 * CEN 3024 - Software Development 1
 * June 25, 2025
 * ErrorCodes.java
 * This class holds all of the error code references to be handled
 * by methods to output the correct error message.
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
