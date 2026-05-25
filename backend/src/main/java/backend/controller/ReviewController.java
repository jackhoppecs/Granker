package backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.apache.catalina.Server;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.ResponseEntity;

import backend.model.Review;
import backend.service.ReviewService;

@RestController
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    
    // 200 OK         → success
    // 201 Created    → new resource created
    // 400 Bad Request→ invalid input
    // 401 Unauthorized
    // 403 Forbidden
    // 404 Not Found
    // 500 Server Error
    
    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    public List<Review> getAllReviews(){
        return reviewService.getAllReviews();
    }

    @GetMapping("/reviews/{id}")
    // I’m returning an HTTP response that contains a Review
    public ResponseEntity<Review> getReviewById(@PathVariable Long id){
        Review review = reviewService.getReviewById(id);

        if (review == null){
            // Status: 404 Not Found, Body: empty
            // .build() means finished creating response and is needed because no body.
            return ResponseEntity.notFound().build();
        }

        // Status: 200 OK, Body: review
        return ResponseEntity.ok(review);
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
    public ResponseEntity<Review> createReview(@PathVariable Long productId, @RequestParam Long userId, @RequestBody Review review){
        Review createdReview = reviewService.createReview(productId, userId, review);
        
        if (createdReview == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(createdReview);
    }

    @GetMapping("/products/{productId}/reviews")
    // If nothing is found [] is returned not null, meaning that the product has no reviews
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId){
        return ResponseEntity.ok(reviewService.getReviewsbyProductId(productId));
    }

    @GetMapping("/users/{userId}/reviews")
    // If nothing is found [] is returned not null, meaning that the user has made no reviews
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(reviewService.getReviewsbyUserId(userId));
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review updatedReview){
        Review review = reviewService.updateReview(id, updatedReview);

        if (review == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id){
        if(reviewService.deleteReview(id)){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
