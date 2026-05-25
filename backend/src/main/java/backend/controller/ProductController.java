package backend.controller;

import backend.model.Product;
import backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    // This handles GET /api/products/1 etc
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        Product product = productService.getProductById(id);
        if (product ==  null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct){
        // Find product by Id, if it exists update attributes of the product with updatedProduct data
        Product product = productService.updateProduct(id, updatedProduct);
        if (product == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
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
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        Product newProduct = productService.createProduct(product);
        return ResponseEntity.ok(newProduct);
    }
}
