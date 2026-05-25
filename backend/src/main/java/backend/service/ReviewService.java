package backend.service;

import backend.model.Product;
import backend.model.User;
import backend.model.Review;


import backend.repository.ProductRepository;
import backend.repository.UserRepository;
import backend.repository.ReviewRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository producRepository;
    private final UserRepository userRepository;

    public ReviewService(
        ReviewRepository reviewRepository,
        ProductRepository productRepository,
        UserRepository userRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.producRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id){
        return reviewRepository.findById(id).orElse(null);
    }

    public Review createReview(Long productId, Long userId, Review review){
        Product product = producRepository.findById(productId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        // Product and user need to exist to create a review.
        if(product == null || user == null){
            return null;
        }
        
        // Set product and user for a review
        review.setProduct(product);
        review.setUser(user);

        // Save to DB
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsbyProductId(Long productId){
        return reviewRepository.findByProductId(productId);
    }

    public List<Review> getReviewsbyUserId(Long userId){
        return reviewRepository.findByUserId(userId);
    }

    public Review updateReview(Long id, Review updatedReview){
        return reviewRepository.findById(id)
        .map(review -> {
            review.setRating(updatedReview.getRating());
            review.setText(updatedReview.getText());
            return reviewRepository.save(review);
        }).orElse(null);
    }

    public boolean deleteReview(Long id){
        
        if(!reviewRepository.existsById(id)){
            return false;
        }

        reviewRepository.deleteById(id);
        return true;
    }

}
