package src;

/**
 * Represents predefined product categories for the inventory system.
 * <p>
 * Each enum constant holds a user-friendly category name.
 * </p>
 *
 * @author Khaleel Zindel Clark
 * @version July 18th, 2025
 */
public enum Category {
    FOOD_BEVERAGES("Food & Beverages"),
    PERSONAL_CARE("Personal Care & Toiletries"),
    ELECTRONICS("Electronics"),
    HOUSEHOLD_ITEMS("Household Items"),
    CLEANING_SUPPLIES("Cleaning Supplies"),
    OFFICE_AND_SCHOOL("Office and School Supplies"),
    CLOTHING("Clothing & Accessories"),
    TOOLS_HARDWARE("Tools & Hardware"),
    HEALTH_WELLNESS("Health & Wellness"),
    ENTERTAINMENT("Entertainment & Media"),
    OUTDOOR_AND_SPORTS("Outdoor & Sports"),
    PET_SUPPLIES("Pet Supplies"),
    MISC("Miscellaneous");

    private final String categoryName;

    /**
     * Constructs a Category enum with a display name.
     *
     * @param categoryName the user-friendly name of the category
     */
    Category(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Returns the string representation of the category,
     * which is its user-friendly name.
     *
     * @return the category name as a String
     */
    @Override
    public String toString() {
        return categoryName;
    }

    /**
     * Returns the string representation of the category,
     * which is its user-friendly name.
     *
     * @return the category name as a String
     */
    public String getCategoryName() {
        return categoryName;
    }


}
