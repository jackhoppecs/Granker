package backend.dto;

import java.time.LocalDateTime;

public class MyReviewResponseDTO {
    private Long reviewId;
    private Integer rating;
    private String text;
    private Long productId;
    private String productName;
    private String productBrand;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MyReviewResponseDTO() {
    }

    public MyReviewResponseDTO(
            Long reviewId,
            Integer rating,
            String text,
            Long productId,
            String productName,
            String productBrand,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.text = text;
        this.productId = productId;
        this.productName = productName;
        this.productBrand = productBrand;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public Integer getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}