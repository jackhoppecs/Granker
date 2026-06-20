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

    // 1.5.0 metadata updates
    private String category;
    private String imageUrl;

    private Integer calories;
    private Integer proteinGrams;
    private Integer carbGrams;
    private Integer fatGrams;

    private String sourceName;
    private String sourceUrl;

    public ProductResponseDTO(
        Long id, 
        String name, 
        String brand, 
        String description,
        Double averageRating, 
        Integer reviewCount, 
        LocalDateTime createdAt, 
        String category,
        String imageUrl,
        Integer calories,
        Integer proteinGrams,
        Integer carbGrams,
        Integer fatGrams,
        String sourceName,
        String sourceUrl
    ){

        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.createdAt = createdAt;

        this.category = category;
        this.imageUrl = imageUrl;
        this.calories = calories;
        this.proteinGrams = proteinGrams;
        this.carbGrams = carbGrams;
        this.fatGrams = fatGrams;
        this.sourceName = sourceName;
        this.sourceUrl = sourceUrl;
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

    public String getCategory(){
        return category;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public Integer getCalories(){
        return calories;
    }

    public Integer getProteinGrams() {
        return proteinGrams;
    }

    public Integer getCarbGrams(){
        return carbGrams;
    }

    public Integer getFatGrams(){
        return fatGrams;
    }

    public String getSourceName(){
        return sourceName;
    }

    public String getSourceUrl(){
        return sourceUrl;
    }
}
