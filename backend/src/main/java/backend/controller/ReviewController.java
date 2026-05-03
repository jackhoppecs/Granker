package backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import backend.model.Review;
import backend.service.ReviewService;

@RestController
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    
    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    public List<Review> getAllReviews(){
        return reviewService.getAllReviews();
    }

    @GetMapping("/reviews/{id}")
    public Review getReviewById(@PathVariable Long id){
        return reviewService.getReviewById(id);
    }
    
    // These endpoints are nested under /products or /users because a Review
    // does not exist independently in this application—it belongs to a specific
    // Product and is written by a specific User.
    //
    // Using routes like:
    //   /products/{productId}/reviews → "reviews for a product"
    //   /users/{userId}/reviews      → "reviews by a user"
    //
    // makes the API more intuitive and RESTful by expressing the relationship
    // between entities in the URL.
    //
    // In contrast, routes like /reviews are used when accessing a review directly
    // (e.g., get by id, update, delete) where no additional context is needed.
    @PostMapping("/products/{productId}/reviews")
    public Review createReview(@PathVariable Long productId, @RequestParam Long userId, @RequestBody Review review){
        return reviewService.createReview(productId, userId, review);
    }

    @GetMapping("/products/{productId}/reviews")
    public List<Review> getReviewsByProductId(@PathVariable Long productId){
        return reviewService.getReviewsbyProductId(productId);
    }

    @GetMapping("/users/{userId}/reviews")
    public List<Review> getReviewsByUserId(@PathVariable Long userId){
        return reviewService.getReviewsbyUserId(userId);
    }

    @PutMapping("/reviews/{id}")
    public Review updateReview(@PathVariable Long id, @RequestBody Review updatedReview){
        return reviewService.updateReview(id, updatedReview);
    }

    @DeleteMapping("/reviews/{id}")
    public void deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
    }
}
