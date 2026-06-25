package backend.importer.openfoodfacts;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.importer.ImportedProductDTO;
import backend.importer.ImportResultDTO;

import java.util.List;

@RestController
public class ProductImportController {
    
    private final ProductImportService productImportService;

    public ProductImportController(ProductImportService productImportService) {
        this.productImportService = productImportService;
    }

    @GetMapping("/api/import/open-food-facts/preview")
    public List<ImportedProductDTO> previewOpenFoodFactsImports() {
        return productImportService.previewFrozenFoodImports();
    }

    @PostMapping("/api/import/open-food-facts")
    public ImportResultDTO importOpenFoodFactsProducts() {
        return productImportService.importFrozenFoodProducts();
    }
}