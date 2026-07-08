package backend.service;

import backend.dto.CreateProductRequest;
import backend.dto.UpdateProductRequest;
import backend.model.Product;
import backend.repository.ProductRepository;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    
    public List<Product> getAllProducts(String sort, Integer minRating, String category, String brand){
        category = normalizeFilter(category);
        brand = normalizeFilter(brand);

        switch (sort) {
            case "newest":
                return productRepository.findAllFilteredOrderByCreatedAtDesc(minRating, category, brand);
            case "highest-rating":
                return productRepository.findAllFilteredOrderByAverageRatingDesc(minRating, category, brand);
            case "most-reviewed":
                return productRepository.findAllFilteredOrderByReviewCountDesc(minRating, category, brand);
            default:
                return productRepository.findAllFilteredOrderByNameAsc(minRating, category, brand);
        }
    }

    private String normalizeFilter(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value;
    }

    public List<String> getCategories(){
        return productRepository.findDistinctCategories();
    }

    public List<String> getBrands(){
        return productRepository.findDistinctBrands();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElse(null);
    }

    public Product updateProduct(Long id, UpdateProductRequest request){
        // Updated product is an entirely new object separate from the one on the DB
        // Therefore we need to update the existing object in the DB and then save that one
        // If we simply saved request it has no id and will create a new reocrd
        // Let's say the id did exist, well we could miss data or overwrite fields not included
        // This is more concise
        return productRepository.findById(id)
        .map(product -> {
            product.setName(request.getName());
            product.setBrand(request.getBrand());
            product.setDescription(request.getDescription());
            return productRepository.save(product);
        }).orElse(null);
    }

    public boolean deleteProduct(Long id){
        if (!productRepository.existsById(id)) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }

    public Product createProduct(CreateProductRequest request){
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

        return productRepository.save(product);
    }
}
