package backend.controller;

import backend.dto.ProductResponseDTO;
import backend.model.Product;
import backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.ArrayList;

// RestController tells spring that this class handles HTTP requests and returns data like JSON
@RestController
// RequestMapping sets the base URL for this controller
// So this controller handles: /api/products
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    // This handles GET /api/products
    @GetMapping
    public List<ProductResponseDTO> getAllProducts(){
        
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDTO> dtos = new ArrayList<>();
        for (Product product : products){
            ProductResponseDTO addProduct = new ProductResponseDTO(product.getId(), product.getName(), product.getBrand(), product.getDescription());
            dtos.add(addProduct);
        }

        return dtos;
    }

    // This handles GET /api/products/1 etc
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id){
        Product product = productService.getProductById(id);
        if (product ==  null){
            return ResponseEntity.notFound().build();
        }

        ProductResponseDTO dto = new ProductResponseDTO(product.getId(), product.getName(), product.getBrand(), product.getDescription());
        return ResponseEntity.ok(dto);
    }
    
    //@Valid @RequestBody Product updatedProduct
    // 1. Read JSON body
    // 2. Convert JSON into Product object
    // 3. Check validation annotations like @NotBlank
    // 4. If valid → run your method
    // 5. If invalid → return 400 Bad Request before your service runs
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updatedProduct){
        // Find product by Id, if it exists update attributes of the product with updatedProduct data
        Product product = productService.updateProduct(id, updatedProduct);
        if (product == null){
            return ResponseEntity.notFound().build();
        }

        ProductResponseDTO dto = new ProductResponseDTO(product.getId(), product.getName(), product.getBrand(), product.getDescription());
        return ResponseEntity.ok(dto);
        // Want to return the updated version of the resource so we can confirm what was changed and we can avoid using another GET request
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        // We don't need a return because we aren't using the data anymore it has been deleted
        if (productService.deleteProduct(id)){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    // Spring takes JSON from request body and turns it into a Product object to be saved to DB
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody Product product){
        Product newProduct = productService.createProduct(product);
        ProductResponseDTO dto = new ProductResponseDTO(newProduct.getId(), newProduct.getName(), newProduct.getBrand(), newProduct.getDescription());
        return ResponseEntity.ok(dto);
    }
}
