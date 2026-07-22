package backend.importer.openfoodfacts;

import org.springframework.web.bind.annotation.*;

import backend.importer.ImportedProductDTO;
import backend.importer.ImportPreviewProductDTO;
import backend.importer.ImportPreviewResponseDTO;
import backend.importer.ImportResultDTO;
import backend.service.AuthService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import javax.swing.Spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ProductImportController {

    private static final Logger logger =
        LoggerFactory.getLogger(ProductImportController.class);
    
    private final ProductImportService productImportService;
    private final AuthService authService;

    public ProductImportController(ProductImportService productImportService, AuthService authService) {
        this.productImportService = productImportService;
        this.authService = authService;
    }

    @GetMapping("/api/import/open-food-facts/preview")
    public ImportPreviewResponseDTO previewOpenFoodFactsImports(
        @RequestParam String category,
        @RequestParam(defaultValue = "10") int pageSize,
        HttpSession session
    ) {
        
        logger.error(
            "IMPORT PREVIEW CONTROLLER REACHED: category={}, pageSize={}",
            category,
            pageSize
        );
        logger.error("ABOUT TO CHECK ADMIN");
        authService.requireAdminUser(session);
        logger.error("ADMIN CHECK PASSED");
        return productImportService.previewFrozenFoodImports(category, pageSize);
    }
    //   React request
    // → browser includes JSESSIONID cookie
    // → Spring resolves HttpSession
    // → authService.requireAdminUser(session)
    // → reads userId from session
    // → loads User from DB
    // → checks user.isAdmin()
    @PostMapping("/api/import/open-food-facts")
    public ImportResultDTO importOpenFoodFactsProducts(
        @RequestParam String category,
        @RequestParam(defaultValue = "10") int pageSize,
        HttpSession session
    ) {
        authService.requireAdminUser(session);
        return productImportService.importFrozenFoodProducts(category, pageSize);
    }
}