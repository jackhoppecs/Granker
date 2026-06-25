package backend.importer.openfoodfacts;

import backend.importer.openfoodfacts.OpenFoodFactsClient;
import backend.importer.openfoodfacts.OpenFoodFactsMapper;
import backend.importer.openfoodfacts.OpenFoodFactsSearchResponse;
import backend.importer.ImportResultDTO;
import backend.importer.ImportedProductDTO;
import backend.model.Product;
import backend.repository.ProductRepository;
import java.time.LocalDateTime;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImportService {

    private final OpenFoodFactsClient openFoodFactsClient;
    private final OpenFoodFactsMapper openFoodFactsMapper;
    private final ProductRepository productRepository;

    public ProductImportService(
            OpenFoodFactsClient openFoodFactsClient,
            OpenFoodFactsMapper openFoodFactsMapper,
            ProductRepository productRepository
    ) {
        this.openFoodFactsClient = openFoodFactsClient;
        this.openFoodFactsMapper = openFoodFactsMapper;
        this.productRepository = productRepository;
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

    public ImportResultDTO importFrozenFoodProducts(){
        List<ImportedProductDTO> dtos = previewFrozenFoodImports();

        int fetched = dtos.size();
        int imported = 0;
        int skippedDuplicates = 0;
        int skippedInvalid = 0;

        for (ImportedProductDTO dto: dtos){
            if (!isUsableImportedProduct(dto)) {
                skippedInvalid++;
                continue;
            }

            boolean alreadyExists = productRepository.existsBySourceNameAndExternalId(
                dto.getSourceName(),
                dto.getExternalId()
            );

            if (alreadyExists){
                skippedDuplicates++;
                continue;
            }

            Product product = toProduct(dto);
            productRepository.save(product);
            imported++;
        }
        return new ImportResultDTO(fetched, imported, skippedDuplicates, skippedInvalid);
    }

    private boolean isUsableImportedProduct(ImportedProductDTO dto) {
        return dto.getExternalId() != null && !dto.getExternalId().isBlank()
                && dto.getName() != null && !dto.getName().isBlank()
                && dto.getBrand() != null && !dto.getBrand().isBlank();
    }

    private Product toProduct(ImportedProductDTO dto){
        Product product = new Product();

        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setDescription(dto.getDescription());
        product.setCategory(dto.getCategory());
        product.setImageUrl(dto.getImageUrl());

        product.setCalories(dto.getCalories());
        product.setProteinGrams(dto.getProteinGrams());
        product.setCarbGrams(dto.getCarbGrams());
        product.setFatGrams(dto.getFatGrams());

        product.setSourceName(dto.getSourceName());
        product.setSourceUrl(dto.getSourceUrl());
        product.setExternalId(dto.getExternalId());
        product.setImportedAt(LocalDateTime.now());

        return product;
    }
}