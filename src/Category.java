package src;

/**
 * Khaleel Zindel Clark
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * Category.java
 * This class creates an enumerator for the user to select
 * from predetermined categories
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

    Category(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * method: getCategoryName
     * parameters: none
     * return: String
     * purpose: this method returns name of the category as a string
     */
    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }

}
