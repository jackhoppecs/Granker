package backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import backend.dto.CreateReviewRequest;
import backend.dto.ReviewResponseDTO;
import backend.dto.MyReviewResponseDTO;
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
    public List<ReviewResponseDTO> getAllReviews(){
        List<Review> reviews = reviewService.getAllReviews();
        List<ReviewResponseDTO> dtos = new ArrayList<>();
        for (Review review : reviews){
            ReviewResponseDTO dto = toReviewResponseDTO(review);
            dtos.add(dto);
        }
        return dtos;
    }

    @GetMapping("/reviews/{id}")
    // I’m returning an HTTP response that contains a Review
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long id){
        Review review = reviewService.getReviewById(id);

        if (review == null){
            // Status: 404 Not Found, Body: empty
            // .build() means finished creating response and is needed because no body.
            return ResponseEntity.notFound().build();
        }
        ReviewResponseDTO response = new ReviewResponseDTO(review.getId(), review.getRating(), review.getText(), review.getUser().getUsername(), review.getProduct().getId(), review.getCreatedAt(), review.getUpdatedAt());
        // Status: 200 OK, Body: review
        return ResponseEntity.ok(response);
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

    // Path variable = identifies the main resource/location
    // Query parameter = extra option/filter/input
    // Request body = data used to create/update the resource
    // Session/auth = identity of the current user
    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<?> createReview(@PathVariable Long productId, @Valid @RequestBody CreateReviewRequest request, HttpSession session){
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Review createdReview = reviewService.createReview(productId, userId, request);

            if (createdReview == null) {
                return ResponseEntity.notFound().build();
            }

            ReviewResponseDTO response = toReviewResponseDTO(createdReview);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @GetMapping("/products/{productId}/reviews")
    // If nothing is found [] is returned not null, meaning that the product has no reviews
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProductId(@PathVariable Long productId, @RequestParam(required = false, defaultValue="name") String sort){
        List<Review> reviews = reviewService.getReviewsbyProductId(productId, sort);
        List<ReviewResponseDTO> dtos = new ArrayList<>();
        
        for (Review review : reviews){
            ReviewResponseDTO response = new ReviewResponseDTO(review.getId(), review.getRating(), review.getText(), review.getUser().getUsername(), review.getProduct().getId(), review.getCreatedAt(), review.getUpdatedAt());
            dtos.add(response);
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/users/{userId}/reviews")
    // If nothing is found [] is returned not null, meaning that the user has made no reviews
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByUserId(@PathVariable Long userId){
        List<Review> reviews = reviewService.getReviewsbyUserId(userId);
        List<ReviewResponseDTO> dtos = new ArrayList<>();

        for (Review review : reviews){
            ReviewResponseDTO response = new ReviewResponseDTO(review.getId(), review.getRating(), review.getText(), review.getUser().getUsername(), review.getProduct().getId(), review.getCreatedAt(), review.getUpdatedAt());
            dtos.add(response);
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/reviews/me")
    public ResponseEntity<List<MyReviewResponseDTO>> getMyReviews(HttpSession session){
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Review> reviews = reviewService.getReviewsbyUserId(userId);

        List<MyReviewResponseDTO> dtos = reviews.stream()
                .map(review -> new MyReviewResponseDTO(
                        review.getId(),
                        review.getRating(),
                        review.getText(),
                        review.getProduct().getId(),
                        review.getProduct().getName(),
                        review.getProduct().getBrand(),
                        review.getCreatedAt(),
                        review.getUpdatedAt()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long id, @Valid @RequestBody CreateReviewRequest request, HttpSession session){
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null){
            return ResponseEntity.status(401).build();
        }

        Review review = reviewService.updateReview(id, request, userId);

        if (review == null){
            return ResponseEntity.notFound().build();
        }

        ReviewResponseDTO response = toReviewResponseDTO(review);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, HttpSession session){
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null){
            return ResponseEntity.status(401).build();
        }

        if(reviewService.deleteReview(id, userId)){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    private ReviewResponseDTO toReviewResponseDTO(Review review) {
        return new ReviewResponseDTO(
                review.getId(),
                review.getRating(),
                review.getText(),
                review.getUser().getUsername(),
                review.getProduct().getId(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
