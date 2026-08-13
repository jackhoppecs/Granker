package backend.controller;

import backend.dto.CreateProductRequest;
import backend.dto.ProductResponseDTO;
import backend.dto.UpdateProductRequest;
import backend.model.Product;
import backend.model.Review;
import backend.service.AuthService;
import backend.service.ProductService;
import backend.service.ReviewService;

import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;

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
    private final AuthService authService;

    public ProductController(ProductService productService, ReviewService reviewService, AuthService authService){
        this.productService = productService;
        this.reviewService = reviewService;
        this.authService = authService;
    }

    public record ProductSliceResponse(
        List<ProductResponseDTO> products,
        boolean hasMore
    ) {}

    // This handles GET /api/products
    @GetMapping
    public ProductSliceResponse getAllProducts(
        // Accept an optional string that decides how to sort products
        @RequestParam(required = false, defaultValue = "name") String sort,
        // Integer is allowed to be null while int is not so using Integer is important
        // defaultValue must always be a string in Spring even if spring is converting
        @RequestParam(required = false) Integer minRating,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String brand,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        
        Slice<Product> productSlice = productService.getAllProducts(sort, minRating, category, brand, search, page, size);
        List<ProductResponseDTO> dtos = new ArrayList<>();
        for (Product product : productSlice.getContent()){
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

        return new ProductSliceResponse(
            dtos,
            productSlice.hasNext()
        );
    }

    // This handles GET /api/products/1 etc
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id){
        Product product = productService.getProductById(id);
        if (product ==  null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
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

    // Functions to retrive all categories and brands for product filtering
    @GetMapping("/categories")
    public List<String> getCategories(){
        return productService.getCategories();
    }

    @GetMapping("/brands")
    public List<String> getBrands(){
        return productService.getBrands();
    }


    //@Valid @RequestBody Product updatedProduct
    // 1. Read JSON body
    // 2. Convert JSON into Product object
    // 3. Check validation annotations like @NotBlank
    // 4. If valid → run your method
    // 5. If invalid → return 400 Bad Request before your service runs
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request, HttpSession session){
        authService.requireAdminUser(session);
        // Find product by Id, if it exists update attributes of the product with updatedProduct data
        Product product = productService.updateProduct(id, request);
        if (product == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
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
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, HttpSession session){
        authService.requireAdminUser(session);
        // We don't need a return because we aren't using the data anymore it has been deleted
        if (productService.deleteProduct(id)){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    // Spring takes JSON from the request body and turns it into a CreateProductRequest DTO.
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody CreateProductRequest request, HttpSession session){
        authService.requireAdminUser(session);
        
        Product newProduct = productService.createProduct(request);

        ProductResponseDTO dto = new ProductResponseDTO(
            newProduct.getId(), 
            newProduct.getName(), 
            newProduct.getBrand(), 
            newProduct.getDescription(), 
            0.0, 
            0, 
            newProduct.getCreatedAt(),

            newProduct.getCategory(),
            newProduct.getImageUrl(),
            newProduct.getCalories(),
            newProduct.getProteinGrams(),
            newProduct.getCarbGrams(),
            newProduct.getFatGrams(),
            newProduct.getSourceName(),
            newProduct.getSourceUrl()
        );
        return ResponseEntity.ok(dto);
    }
}
