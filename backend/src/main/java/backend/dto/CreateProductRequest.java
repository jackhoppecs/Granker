package backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateProductRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @Size(max = 2000)
    private String description;
    
    @Size(max = 200)
    private String category;

    @Size(max = 500)
    private String imageUrl;

    @Min(0)
    private Integer calories;
    @DecimalMin("0.0")
    private Double proteinGrams;
    @DecimalMin("0.0")
    private Double carbGrams;
    @DecimalMin("0.0")
    private Double fatGrams;

    @Size(max = 100)
    private String sourceName;
    @Size(max = 500)
    private String sourceUrl;

    public CreateProductRequest(){

    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Double getProteinGrams() {
        return proteinGrams;
    }

    public void setProteinGrams(Double proteinGrams) {
        this.proteinGrams = proteinGrams;
    }

    public Double getCarbGrams() {
        return carbGrams;
    }

    public void setCarbGrams(Double carbGrams) {
        this.carbGrams = carbGrams;
    }

    public Double getFatGrams() {
        return fatGrams;
    }

    public void setFatGrams(Double fatGrams) {
        this.fatGrams = fatGrams;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
