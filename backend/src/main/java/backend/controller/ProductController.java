package backend.controller;

import backend.dto.CreateProductRequest;
import backend.dto.ProductResponseDTO;
import backend.model.Product;
import backend.model.Review;
import backend.service.ProductService;
import backend.service.ReviewService;

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
    private final ReviewService reviewService;

    public ProductController(ProductService productService, ReviewService reviewService){
        this.productService = productService;
        this.reviewService = reviewService;
    }

    // This handles GET /api/products
    @GetMapping
    public List<ProductResponseDTO> getAllProducts(
        // Accept an optional string that decides how to sort products
        @RequestParam(required = false, defaultValue = "name") String sort,
        // Integer is allowed to be null while int is not so using Integer is important
        // defaultValue must always be a string in Spring even if spring is converting
        @RequestParam(required = false) Integer minRating
    ) {
        
        List<Product> products = productService.getAllProducts(sort, minRating);
        List<ProductResponseDTO> dtos = new ArrayList<>();
        for (Product product : products){
            // Will need to eventually call repository function in service instead
            // Also a N + 1 pattern for queries, we can optimize later
            List<Review> reviews = reviewService.getReviewsByProductId(product.getId());
            double averageRating = reviews.stream()
            .mapToInt(Review::getRating).average().orElse(0.0);
            Integer reviewCount = reviews.size();
            ProductResponseDTO addProduct = new ProductResponseDTO(
                product.getId(), 
                product.getName(), 
                product.getBrand(), 
                product.getDescription(), 
                averageRating, 
                reviewCount, 
                product.getCreatedAt(),

                product.getCategory(),
                product.getImageUrl(),
                product.getCalories(),
                product.getProteinGrams(),
                product.getCarbGrams(),
                product.getFatGrams(),
                product.getSourceName(),
                product.getSourceUrl()
            );
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

        List<Review> reviews = reviewService.getReviewsByProductId(product.getId());
        double averageRating = reviews.stream()
            .mapToInt(Review::getRating).average().orElse(0.0);
        Integer reviewCount = reviews.size();
        ProductResponseDTO dto = new ProductResponseDTO(
            product.getId(), 
            product.getName(), 
            product.getBrand(), 
            product.getDescription(), 
            averageRating, 
            reviewCount, 
            product.getCreatedAt(),

            product.getCategory(),
            product.getImageUrl(),
            product.getCalories(),
            product.getProteinGrams(),
            product.getCarbGrams(),
            product.getFatGrams(),
            product.getSourceName(),
            product.getSourceUrl()
        );
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

        List<Review> reviews = reviewService.getReviewsByProductId(product.getId());
        double averageRating = reviews.stream()
            .mapToInt(Review::getRating).average().orElse(0.0);
        Integer reviewCount = reviews.size();
        ProductResponseDTO dto = new ProductResponseDTO(
            product.getId(), 
            product.getName(),
            product.getBrand(), 
            product.getDescription(), 
            averageRating, 
            reviewCount, 
            product.getCreatedAt(),

            product.getCategory(),
            product.getImageUrl(),
            product.getCalories(),
            product.getProteinGrams(),
            product.getCarbGrams(),
            product.getFatGrams(),
            product.getSourceName(),
            product.getSourceUrl()
        
        );
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
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody CreateProductRequest request){
        Product product = new Product(
            request.getName(), 
            request.getBrand(), 
            request.getDescription()
        ); 

        // Set optional fields
        // If we made the constructor huge we would have to keep everything in order which can become annoying and lead to mistakes
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
        product.setCalories(request.getCalories());
        product.setProteinGrams(request.getProteinGrams());
        product.setCarbGrams(request.getCarbGrams());
        product.setFatGrams(request.getFatGrams());
        product.setSourceName(
            request.getSourceName() == null || request.getSourceName().isBlank()
                ? "User submitted"
                : request.getSourceName()
        );
        product.setSourceUrl(request.getSourceUrl());
        Product newProduct = productService.createProduct(product);

        ProductResponseDTO dto = new ProductResponseDTO(
            newProduct.getId(), 
            newProduct.getName(), 
            newProduct.getBrand(), 
            newProduct.getDescription(), 
            0.0, 
            0, 
            newProduct.getCreatedAt(),

            product.getCategory(),
            product.getImageUrl(),
            product.getCalories(),
            product.getProteinGrams(),
            product.getCarbGrams(),
            product.getFatGrams(),
            product.getSourceName(),
            product.getSourceUrl()
        );
        return ResponseEntity.ok(dto);
    }
}
