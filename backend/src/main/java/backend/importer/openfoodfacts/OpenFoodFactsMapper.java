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
        dto.setCategory(response.getCategories());
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
}