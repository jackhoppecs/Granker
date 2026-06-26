package backend.importer.openfoodfacts;

import org.springframework.web.bind.annotation.*;

import backend.importer.ImportedProductDTO;
import backend.importer.ImportPreviewProductDTO;
import backend.importer.ImportPreviewResponseDTO;
import backend.importer.ImportResultDTO;

import java.util.List;

@RestController
public class ProductImportController {
    
    private final ProductImportService productImportService;

    public ProductImportController(ProductImportService productImportService) {
        this.productImportService = productImportService;
    }

    @GetMapping("/api/import/open-food-facts/preview")
    public ImportPreviewResponseDTO previewOpenFoodFactsImports(
        @RequestParam String category,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return productImportService.previewFrozenFoodImports(category, pageSize);
    }

    @PostMapping("/api/import/open-food-facts")
    public ImportPreviewResponseDTO importOpenFoodFactsProducts(
        @RequestParam String category,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return productImportService.importFrozenFoodProducts(category, pageSize);
    }
}