package backend.service;

import backend.dto.CreateReviewRequest;
import backend.model.Product;
import backend.model.User;
import backend.model.Review;


import backend.repository.ProductRepository;
import backend.repository.UserRepository;
import backend.repository.ReviewRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    public Review createReview(Long productId, Long userId, CreateReviewRequest request){
        Product product = producRepository.findById(productId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        // Product and user need to exist to create a review.
        if(product == null || user == null){
            return null;
        }

        // Check for duplicate reviews on one product
        if (reviewRepository.existsByProductIdAndUserId(productId, userId)){
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "You have already reviewed this product."
            );
        }

        // Set product and user for a review
        Review review = new Review();
        review.setRating(request.getRating());
        review.setText(request.getText());
        review.setProduct(product);
        review.setUser(user);

        // Save to DB
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsbyProductId(Long productId, String sort){
        switch (sort) {
            case "newest":
                return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
            case "lowest-rating":
                return reviewRepository.findByProductIdOrderByRatingAsc(productId);  
            case "highest-rating":
                return reviewRepository.findByProductIdOrderByRatingDesc(productId);
            default:
                return reviewRepository.findByProductId(productId);
        }
    }

    public List<Review> getReviewsbyUserId(Long userId){
        return reviewRepository.findByUserId(userId);
    }

    public Review updateReview(Long id, CreateReviewRequest request, Long userId){
        return reviewRepository.findById(id)
        .map(review -> {

            if (!review.getUser().getId().equals(userId)){
                throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to update this review."
                );
            }

            review.setRating(request.getRating());
            review.setText(request.getText());

            return reviewRepository.save(review);
        }).orElse(null);
    }

    public boolean deleteReview(Long id, Long userId){
        Optional<Review> optionalReview = reviewRepository.findById(id);
        
        // Check if review exists
        if(optionalReview.isEmpty()){
            return false;
        }

        Review review = optionalReview.get();

        // Check if current review belongs to current session
        if(!review.getUser().getId().equals(userId)){
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "You are not allowed to delete this review."
            );
        }

        // Delete and return that we succesfully deleted review
        reviewRepository.delete(review);
        return true;
    }

    public List<Review> getReviewsByProductId(Long productId){
        return reviewRepository.findByProductId(productId);
    }
}
