package backend.repository;

import backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// JPA repostiory basically gives you a bunch of functions for free that are basically all CRUD
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
