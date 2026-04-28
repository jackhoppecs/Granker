package backend.controller;

import backend.model.Product;
import backend.service.ProductService;
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
    public Product getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct){
        // Find product by Id, if it exists update attributes of the product with updatedProduct data
        return productService.updateProduct(id, updatedProduct);
        // Want to return the updated version of the resource so we can confirm what was changed and we can avoid using another GET request
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        // We don't need a return because we aren't using the data anymore it has been deleted
        productService.deleteProduct(id);
    }

    @PostMapping
    // Spring takes JSON from request body and turns it into a Product object to be saved to DB
    public Product createProduct(@RequestBody Product product){
        return productService.createProduct(product);
    }
}
