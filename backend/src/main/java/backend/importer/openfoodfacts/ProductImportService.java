package backend.importer.openfoodfacts;

import backend.importer.openfoodfacts.OpenFoodFactsClient;
import backend.importer.openfoodfacts.OpenFoodFactsMapper;
import backend.importer.openfoodfacts.OpenFoodFactsSearchResponse;
import backend.importer.openfoodfacts.SupportedImportCategory;
import backend.importer.ImportPreviewProductDTO;
import backend.importer.ImportPreviewResponseDTO;
import backend.importer.ImportResultDTO;
import backend.importer.ImportedProductDTO;
import backend.model.Product;
import backend.repository.ProductRepository;
import java.time.LocalDateTime;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public ImportPreviewResponseDTO previewFrozenFoodImports(String category, int pageSize){
        long totalStart = System.currentTimeMillis();
        validateImportRequest(category, pageSize);
        SupportedImportCategory supportedCategory = SupportedImportCategory.fromRequestValue(category);
        long offStart = System.currentTimeMillis();
        OpenFoodFactsSearchResponse response = openFoodFactsClient.searchProductsByCategory(supportedCategory.getOpenFoodFactsTag(), pageSize);
        System.out.println("OFF fetch ms: " + (System.currentTimeMillis() - offStart));


        // Need to initalize and return a ImportPreviewResponseDTO if no response/products
        if (response == null || response.getProducts() == null) {
            return new ImportPreviewResponseDTO(
                supportedCategory.getRequestValue(),
                supportedCategory.getDisplayName(),
                0,
                0,
                0,
                List.of()
            );
        }

        

        // Java stream pipeline
        // starts with raw Openfoodfacts products and ends with a clean list of ImportedProductDTO
        // return response.getProducts()
        //         .stream()
        //         // converts each raw Open Food Facs product into my DTO
        //         // Shorthand for product -> openFoodFactsMapper.toImportedProductDTO(product)
        //         .map(openFoodFactsMapper::toImportedProductDTO)
        //         // modifies DTO as it passes through the stream 
        //         .peek(dto -> dto.setCategory(supportedCategory.getNormalizedCategory()))
        //         // This keeps only DTO's that pass the validation method
        //         .filter(this::isUsableImportedProduct)
        //         .toList();

        List<ImportPreviewProductDTO> previewProducts = response.getProducts()
            .stream()
            .map(openFoodFactsMapper::toImportedProductDTO)
            .map(dto -> {
                dto.setCategory(supportedCategory.getNormalizedCategory());
                return buildPreviewProduct(dto);
            })
            .toList();
            
        int importableCount = (int) previewProducts.stream()
            .filter(ImportPreviewProductDTO::isImportable)
            .count();
        
        int skippedCount = previewProducts.size() - importableCount;
        System.out.println("Total preview ms: " + (System.currentTimeMillis() - totalStart));
        return new ImportPreviewResponseDTO(
            supportedCategory.getRequestValue(),
            supportedCategory.getDisplayName(),
            previewProducts.size(),
            importableCount,
            skippedCount,
            previewProducts
        );
    }

    public ImportResultDTO importFrozenFoodProducts(String category, int pageSize) {
        ImportPreviewResponseDTO preview = previewFrozenFoodImports(category, pageSize);

        int skippedDuplicates = 0;
        int skippedInvalid = 0;

        List<Product> productsToSave = new ArrayList<>();

        for (ImportPreviewProductDTO previewProduct : preview.getProducts()) {
            if (previewProduct.isImportable()) {
                ImportedProductDTO dto = previewProduct.getProduct();
                productsToSave.add(toProduct(dto));
            } else {
                if (previewProduct.getSkipReasons().contains("Already imported")) {
                    skippedDuplicates++;
                } else {
                    skippedInvalid++;
                }
            }
        }

        List<Product> savedProducts = productRepository.saveAll(productsToSave);

        return new ImportResultDTO(
            preview.getFetchedCount(),
            savedProducts.size(),
            skippedDuplicates,
            skippedInvalid
        );
    }

    // HELPER FUNCTIONS

    private ImportPreviewProductDTO buildPreviewProduct(ImportedProductDTO dto) {
        List<String> skipReasons = getSkipReasons(dto);

        return new ImportPreviewProductDTO(
                dto,
                skipReasons.isEmpty(),
                skipReasons
        );
    }

    private List<String> getSkipReasons(ImportedProductDTO dto) {
        List<String> reasons = new ArrayList<>();

        if (dto.getExternalId() == null || dto.getExternalId().isBlank()) {
            reasons.add("Missing external ID");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            reasons.add("Missing product name");
        }

        if (dto.getBrand() == null || dto.getBrand().isBlank()) {
            reasons.add("Missing brand");
        }

        long duplicateStart = System.currentTimeMillis();

        boolean alreadyExists = dto.getSourceName() != null
                && dto.getExternalId() != null
                && productRepository.existsBySourceNameAndExternalId(
                        dto.getSourceName(),
                        dto.getExternalId()
                );

        if (alreadyExists) {
            reasons.add("Already imported");
        }
        System.out.println("Duplicate check ms: " + (System.currentTimeMillis() - duplicateStart));
        return reasons;
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

    private void validateImportRequest(String category, int pageSize) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category is required.");
        }

        if (pageSize < 1 || pageSize > 50) {
            throw new IllegalArgumentException("Page size must be between 1 and 50.");
        }
    }
}