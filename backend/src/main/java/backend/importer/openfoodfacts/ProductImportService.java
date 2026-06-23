package backend.importer.openfoodfacts;

import backend.importer.openfoodfacts.OpenFoodFactsClient;
import backend.importer.openfoodfacts.OpenFoodFactsMapper;
import backend.importer.openfoodfacts.OpenFoodFactsSearchResponse;

import backend.importer.ImportedProductDTO;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImportService {

    private final OpenFoodFactsClient openFoodFactsClient;
    private final OpenFoodFactsMapper openFoodFactsMapper;

    public ProductImportService(
            OpenFoodFactsClient openFoodFactsClient,
            OpenFoodFactsMapper openFoodFactsMapper
    ) {
        this.openFoodFactsClient = openFoodFactsClient;
        this.openFoodFactsMapper = openFoodFactsMapper;
    }

    public List<ImportedProductDTO> previewFrozenFoodImports(){
        OpenFoodFactsSearchResponse response = openFoodFactsClient.searchFrozenFoods();

        if (response == null || response.getProducts() == null) {
            return List.of();
        }

        return response.getProducts()
                .stream()
                .map(openFoodFactsMapper::toImportedProductDTO)
                .filter(this::isUsableImportedProduct)
                .toList();
    }

    private boolean isUsableImportedProduct(ImportedProductDTO dto) {
        return dto.getExternalId() != null && !dto.getExternalId().isBlank()
                && dto.getName() != null && !dto.getName().isBlank()
                && dto.getBrand() != null && !dto.getBrand().isBlank();
    }
}