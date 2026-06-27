package backend.importer;

public class ImportedProductDTO {
    private String externalId;
    private String name;
    private String brand;
    private String description;
    private String category;
    private String imageUrl;

    private Integer calories;
    private Double proteinGrams;
    private Double carbGrams;
    private Double fatGrams;

    private String sourceName;
    private String sourceUrl;

    public ImportedProductDTO() {
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
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