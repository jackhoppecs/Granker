package backend.service;

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
    
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElse(null);
    }

    public Product updateProduct(Long id, Product updatedProduct){
        // Updated product is an entirely new object separate from the one on the DB
        // Therefore we need to update the existing object in the DB and then save that one
        // If we simply saved updatedProduct it has no id and will create a new reocrd
        // Let's say the id did exist, well we could miss data or overwrite fields not included
        // This is more concise
        return productRepository.findById(id)
        .map(product -> {
            product.setName(updatedProduct.getName());
            product.setBrand(updatedProduct.getBrand());
            product.setDescription(updatedProduct.getDescription());
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

    public Product createProduct(Product product){
        return productRepository.save(product);
    }




}
