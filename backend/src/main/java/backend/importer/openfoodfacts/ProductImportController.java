package backend.importer.openfoodfacts;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.importer.ImportedProductDTO;

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
}