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

    public String getCategoryName() {
        return categoryName;
    }
}
