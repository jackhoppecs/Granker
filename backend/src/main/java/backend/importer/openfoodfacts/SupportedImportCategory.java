package backend.importer.openfoodfacts;

// An enum is a type where the allowed values are predefined
// So instead of allowing random string we create a fixed set: "FROZEN_PIZZA", "ICE_CREAM"
public enum SupportedImportCategory {
    // Enum options (Enum name: frontend/request value, display label, open food facts tag, db category)
    FROZEN_PIZZA("frozen-pizza", "Frozen Pizza", "en:frozen-pizzas", "Frozen Pizza"),
    FROZEN_BURRITOS("frozen-burritos", "Frozen Burritos", "en:frozen-burritos", "Frozen Burritos"),
    FROZEN_VEGETABLES("frozen-vegetables", "Frozen Vegetables", "en:frozen-vegetables", "Frozen Vegetables"),
    FROZEN_MEALS("frozen-meals", "Frozen Meals", "en:frozen-meals", "Frozen Meals"),
    ICE_CREAM("ice-cream", "Ice Cream", "en:ice-creams", "Ice Cream");

    private final String requestValue;
    private final String displayName;
    private final String openFoodFactsTag;
    private final String normalizedCategory;

    SupportedImportCategory(
        String requestValue,
        String displayName,
        String openFoodFactsTag,
        String normalizedCategory
    ) {
        this.requestValue = requestValue;
        this.displayName = displayName;
        this.openFoodFactsTag = openFoodFactsTag;
        this.normalizedCategory = normalizedCategory;
    }

    public String getRequestValue() {
        return requestValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getOpenFoodFactsTag() {
        return openFoodFactsTag;
    }

    public String getNormalizedCategory() {
        return normalizedCategory;
    }

    public static SupportedImportCategory fromRequestValue(String value) {
        // Compare each categories request value "FROZEN PIZZA" -> "frozen-pizza" to the parameter -> "frozen_pizza"
        for (SupportedImportCategory category : values()) {
            if (category.requestValue.equalsIgnoreCase(value)) {
                // Then you can call "FROZEN_PIZZA".getDisplayName or .getNormalizedCategroy etc
                return category;
            }
        }

        throw new IllegalArgumentException("Unsupported import category: " + value);
    }
}
