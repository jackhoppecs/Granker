package backend.repository;

import backend.model.Review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository  extends JpaRepository<Review, Long>{
    
    List<Review> findByProductId(Long productId);

    List<Review> findByUserId(Long userId);

    boolean existsByProductIdAndUserId(Long productId, Long userId);
    
}
