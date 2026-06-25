package backend.importer.openfoodfacts;

import backend.importer.ImportedProductDTO;
import org.springframework.stereotype.Component;

@Component
public class OpenFoodFactsMapper{

    public ImportedProductDTO toImportedProductDTO(OpenFoodFactsProductResponse response) {
        ImportedProductDTO dto = new ImportedProductDTO();

        dto.setExternalId(response.getCode());
        dto.setName(response.getProductName());
        dto.setBrand(response.getBrands());
        dto.setDescription(null);
        dto.setCategory(normalizeCategory(response.getCategories()));
        dto.setImageUrl(response.getImageUrl());

        dto.setSourceName("Open Food Facts");
        dto.setSourceUrl(response.getUrl());

        if (response.getNutriments() != null) {
            dto.setCalories(toInteger(response.getNutriments().getCalories()));
            dto.setProteinGrams(response.getNutriments().getProteinGrams());
            dto.setCarbGrams(response.getNutriments().getCarbGrams());
            dto.setFatGrams(response.getNutriments().getFatGrams());
        }

        return dto;
    }

    private Integer toInteger(Double value) {
        if (value == null) {
            return null;
        }

        return (int) Math.round(value);
    }

    private String normalizeCategory(String categories) {
        if (categories == null || categories.isBlank()) {
            return "Other";
        }

        String lower = categories.toLowerCase();

        if (lower.contains("frozen pizza")) {
            return "Frozen Pizza";
        }

        if (lower.contains("frozen ready-made meals")) {
            return "Frozen Meal";
        }

        if (lower.contains("ice cream")) {
            return "Ice Cream";
        }

        if (lower.contains("frozen vegetables")) {
            return "Frozen Vegetables";
        }

        return "Other";
    }
}