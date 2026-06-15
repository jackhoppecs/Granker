package backend.dto;

import java.time.LocalDateTime;

public class ProductResponseDTO {
    
    private Long id;
    private String name;
    private String brand;
    private String description;
    private Double averageRating;
    private Integer reviewCount;
    LocalDateTime createdAt;

    public ProductResponseDTO(Long id, String name, String brand, String description, Double averageRating, Integer reviewCount, LocalDateTime createdAt){
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.createdAt = createdAt;
    }

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getBrand(){
        return brand;
    }

    public String getDescription(){
        return description;
    }

    // Spring/Jackson which converts data to JSON requires Camelcase
    // so getAverageRating not getaverageRating, otherwise it will compile but won't be picked up automatically
    public Double getAverageRating(){
        return averageRating;
    }

    public Integer getReviewCount(){
        return reviewCount;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
}
